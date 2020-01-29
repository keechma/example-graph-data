(ns keechma-graph-data.core
  (:require [clojure.string :as str]
            [entitydb.core :as edb]
            [keechma.app-state :as app-state]
            [keechma.controller :as controller]
            [keechma.ui-component :as ui])
  (:require-macros [reagent.ratom :refer [reaction]]))

(enable-console-print!)

(def user-data
  "Define the initial graph data"
  [{:id 1 :username "User #1" :favorite-users [2 3]}
   {:id 2 :username "User #2" :favorite-users [1]}
   {:id 3 :username "User #3" :favorite-users [2 1 3]}])

(defn process-user-data
  "Process the data so the EntityDB knows how to handle relations

  This function transforms `[1,2,3]` to [{:id 1}, {:id 2}, {:id 3}].
  When the data is inserted into the EntityDB, it will set up the links
  to the real user entities."
  [user-data]
  (map (fn [user]
         (assoc user :favorite-users
                (map (fn [favorite-user]
                       {:id favorite-user}) (:favorite-users user)))) user-data))

(def schema
  "Set up the schema so users have relations to their favorite users"
  {:users {:id :id
           :relations {:favorite-users [:many :users]}}})

(defrecord DataController [])

(defmethod controller/start DataController
  [_ _ app-db]
  ;; Inserts the data into the application state
  (edb/insert-collection schema app-db :users :list (process-user-data user-data)))

(defn user-list
  "Get the user list from the app state"
  [app-db]
  (reaction
   (edb/get-collection schema @app-db :users :list)))

(defn user-renderer
  "Renders one user and a list of it's favorite users.

  `(:favorite-users user)` returns a function which will return
  the list of favorite users for a user. This allows EntityDB to
  handle circular relations."
  [user]
  [:tr {:key (:id user)}
   [:td (:username user)]
   [:td (str/join ", " (map #(:username %) ((:favorite-users user))))]])

(defn user-list-renderer
  "Render the list of users. Each user is rendered by the `user-renderer`
  function."
  [ctx]
  (fn []
    (let [user-list-sub (ui/subscription ctx :user-list)]
      [:table.table
       [:thead
        [:tr
         [:th "User"]
         [:th "Favorite Users"]]]
       [:tbody
        (map user-renderer @user-list-sub)]])))

(def user-list-component
  "Defines the renderer and subscription dependencies for
  the user list component."
  (ui/constructor {:renderer user-list-renderer
                   :subscription-deps [:user-list]}))

(def app-definition {:controllers {:data (->DataController)}
                     :subscriptions {:user-list user-list}
                     :components {:main user-list-component}
                     :html-element (.getElementById js/document "app")})

(defonce running-app (clojure.core/atom nil))

(defn start-app!
  "Helper function that starts the application."
  []
  (reset! running-app (app-state/start! app-definition)))

(defn restart-app!
  "Helper function that restarts the application whenever the
  code is hot reloaded."
  []
  (let [current @running-app]
    (if current
      (app-state/stop! current start-app!)
      (start-app!))))

(restart-app!)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
