# reggae [![Build Status][travis-badge]][travis][![Dependencies Status][deps-badge]][deps][![Clojars Project][clojars-badge]][clojars][![Clojure version][clojure-v]](project.clj)

*A Clojure wrapper for the Rasdaman Java Client Library*

[![][logo]][logo-large]


**NOTICE**: This library is a work in progress and general experiment. Please don't expect anything to work! That being said, feel free to submit tickets for features you'd like to see, missing methods you'd like wrapped, etc.


##### Contents

* [Introduction](#introduction-)
* [Dependencies](#dependencies-)
* [Installation](#installation-)
* [Usage](#usage-)
  * [Startup](#startup-)
  * [Connecting](#connecting-)
  * [Querying](#querying-)
* [Resources](#resources-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

The [Rasdaman site](http://www.rasdaman.org/) touts the database as the World's Leading Array Database, allowing users to store and query massive multi-dimensional ​arrays such as sensor, image, simulation, and statistics data appearing in domains like earth, space, and life science. During a review in Fall 2014, independent experts unanimously attested that, based on "proven evidence", rasdaman will "significantly transform the way scientists access and use data in a way that hitherto was not possible".

Rasdaman comes with client libraries written in C++ and Java. The Clojure reggae library is a thin wrapper around the Java library, allowing for idiomatic Clojure when working with Rasdaman.


## Dependencies [&#x219F;](#contents)

 * A running instance of Rasdaman
 * PostgreSQL and postgis
 * The Rasdaman PostgreSQL backend configured to accept network connections
 * ``lein``


## Installation [&#x219F;](#contents)

Add the following to your ``project.clj`` file's ``:dependencies``:

[![Clojars Project][clojars-badge]][clojars]

Then, in the namespace where you want to use Reggae:

```clj
(ns your.gis.project
  (:require [reggae.core :as reggae]))
```


## Usage [&#x219F;](#contents)

### Startup [&#x219F;](#contents)

```
$ lein repl
```


### Connecting [&#x219F;](#contents)

```clj
reggae.dev=> (require '[reggae.core :as reggae])
nil
reggae.dev=> (def rashost "127.0.0.1")
#'reggae.dev/rashost
reggae.dev=> (def client (reggae/make-client :host rashost))
#'reggae.dev/client
reggae.dev=>
```


### Querying [&#x219F;](#contents)

```clj
(require '[reggae.types :as types])
reggae.dev=> (def query-str "select sdom(m) from Multiband as m")
#'reggae.dev/query-str
reggae.dev=> (def result (reggae/query client query-str))
#'reggae.dev/result
reggae.dev=> (map types/interval->vector result)
([4657 4923] [4657 4923] [4657 4923] [4657 4923] [4657 4923]
 [4657 4923] [4657 4923] [4657 4923] [17 18])
```

Or, with no assignments:

```clj
reggae.dev=> (->> "select sdom(m) from Multiband as m"
                  (reggae/query client)
                  (map types/interval->vector))
([4657 4923] [4657 4923] [4657 4923] [4657 4923] [4657 4923]
 [4657 4923] [4657 4923] [4657 4923] [17 18])
```

In that example we used a convenience function from the ``types`` namespace.
You still have access to all the Rasdaman objects (via idiomatic Clojure
wrappers in ``reggae.rasj``), should you wish to use those:

```clj
reggae.dev=> (require '[reggae.rasj.types.interval :as rinterval]
                      '[reggae.rasj.types.point :as rpoint])
nil
```

Work with an interval:

```clj
reggae.dev=> (def first-interval (first result))
#'reggae.dev/first-interval
reggae.dev=> first-interval
#object[rasj.RasMInterval 0x1c2d6398 "[0:4656,0:4922]"]
```

Work with a point:

```clj
reggae.dev=> (rinterval/get-extent first-interval)
#object[rasj.RasPoint 0x3125fd2d "[4657,4923]"]
reggae.dev=> (-> first-interval
                 (rinterval/get-extent)
                 (rpoint/->vector))
[4657 4923]
```


## Resources [&#x219F;](#contents)

* [Rasdaman Java Developers Guide](http://www.rasdaman.org/browser/manuals_and_examples/manuals/doc-guides/dev-guide-java.pdf)
* [Rasdaman Query Language Guide](http://www.rasdaman.org/browser/manuals_and_examples/manuals/doc-guides/ql-guide.pdf)
* [Rasdaman Quickstart](https://live.osgeo.org/en/quickstart/rasdaman_quickstart.html)
* Deprecated:
  * [Tutorial - Spatio-Temporal Big Data](http://www.rasdaman.org/attachment/wiki/Workshops/BigDataRasdamanApproach/BigDataRasdamanWorkshop.pdf) - Rasdaman Big Data Workshop
  * [Shell Exercises](http://www.rasdaman.org/attachment/wiki/Workshops/BigDataRasdamanApproach/LOG)

## License [&#x219F;](#contents)

Copyright © 2015-2016 Duncan McGreggor

Distributed under the Eclipse Public License, the same as Clojure.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/reggae
[travis-badge]: https://travis-ci.org/clojusc/reggae.png?branch=master
[deps]: http://jarkeeper.com/clojusc/reggae
[deps-badge]: http://jarkeeper.com/clojusc/reggae/status.svg
[logo]: resources/images/clj-reggea-logo-3.png
[logo-large]: resources/images/clj-reggea-logo-3-large.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/reggae.svg?maxAge=2592000
[tag]: https://github.com/clojusc/reggae/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[clojars]: https://clojars.org/clojusc/reggae
[clojars-badge]: https://img.shields.io/clojars/v/clojusc/reggae.svg
