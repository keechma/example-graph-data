# keechma-graph-data

Example application that shows how to render the graph data with the [Keechma framework](http://github.com/keechma/keechma).

Read the [annotated source](http://keechma.com/annotated/graph-data.html).

## Setup

Make sure you have [Leiningen](http://leiningen.org/) installed.

Clone the repo:

```
$ git clone https://github.com/keechma/keechma-graph-data.git
$ cd keechma-graph-data
```

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL.

## License

Copyright © 2016 Mihael Konjevic

Distributed under the MIT License.# keechma-graph-data

FIXME: Write a one-line description of your library/project.

## Overview

FIXME: Write a paragraph about the library/project and highlight its goals.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
