# reggae [![Build Status][travis-badge]][travis][![GitHub tag][github-tag]]()[![Dependencies Status][deps-badge]][deps][![Clojure versions][clojure-v]]()

*A Clojure wrapper for the Rasdaman Java Client Library*

[![][logo]][logo-large]


**NOTICE**: This library is a work in progress and general experiment. Please don't expect anything to work! That being said, feel free to submit tickets for features you'd like to see, missing methods you'd like wrapped, etc.


##### Contents

* [Introduction](#introduction-)
* [Dependencies](#dependencies-)
* [Usage](#usage-)
  * [Startup](#startup-)
  * [Connecting](#connecting-)
  * [Querying](#querying-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

The [Rasdaman site](http://www.rasdaman.org/) touts the database as the World's Leading Array Database, allowing users to store and query massive multi-dimensional ​arrays such as sensor, image, simulation, and statistics data appearing in domains like earth, space, and life science. During a review in Fall 2014, independent experts unanimously attested that, based on "proven evidence", rasdaman will "significantly transform the way scientists access and use data in a way that hitherto was not possible".

Rasdaman comes with client libraries written in C++ and Java. The Clojure reggae library is a thin wrapper around the Java library, allowing for idiomatic Clojure when working with Rasdaman.


## Dependencies [&#x219F;](#contents)

 * A running instance of Rasdaman
 * PostgreSQL and postgis
 * The Rasdaman PostgreSQL backend configured to accept network connections
 * ``lein``


## Usage [&#x219F;](#contents)

### Startup

```
$ lein repl
```


### Connecting

```clojure
reggae.dev=> (require '[reggae.core :as reggae])
nil
reggae.dev=> (def rashost "127.0.0.1")
#'reggae.dev/rashost
reggae.dev=> (def client (reggae/make-client :host rashost))
#'reggae.dev/client
reggae.dev=>
```


### Querying

```clojure
=> (def r (reggae/query client "select sdom(m) from Multiband as m"))
#'r
=> (type r)
rasj.odmg.RasBag
=> (.size r)
9
=> (def r1 (first (take 1 r)))
#'r1
=> (type r1)
rasj.RasMInterval
=> (.dimension r1)
2
=> (.low (.item r1 0))
0
=> (.high (.item r1 0))
4656
=> (.low (.item r1 1))
0
=> (.high (.item r1 1))
4922
```


## License [&#x219F;](#contents)

Copyright © 2016 Duncan McGreggor

Distributed under the Eclipse Public License, the same as Clojure.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/reggae
[travis-badge]: https://travis-ci.org/clojusc/reggae.png?branch=master
[deps]: http://jarkeeper.com/clojusc/reggae
[deps-badge]: http://jarkeeper.com/clojusc/reggae/status.svg
[logo]: resources/images/clj-reggea-logo-3.png
[logo-large]: resources/images/clj-reggea-logo-3-large.png
[github-tag]: https://img.shields.io/github/tag/clojusc/reggae.svg?maxAge=2592000
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
