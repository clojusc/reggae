# reggae

*A Clojure wrapper for the Rasdaman Java Client Library*

[![][logo]][logo-large]

[logo]: resources/images/clj-reggea-logo-3.png
[logo-large]: resources/images/clj-reggea-logo-3-large.png


##### Contents

* [Introduction](#introduction-)
* [Dependencies](#dependencies-)
* [Usage](#usage-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

The [Rasdaman site](http://www.rasdaman.org/) touts the database as the World's Leading Array Database, allowing users to store and query massive multi-dimensional ​arrays such as sensor, image, simulation, and statistics data appearing in domains like earth, space, and life science. During a review in Fall 2014, independent experts unanimously attested that, based on "proven evidence", rasdaman will "significantly transform the way scientists access and use data in a way that hitherto was not possible".

Rasdaman comes with client libraries written in C++ and Java. The Clojure reggae library is a thing wrapper around the Java library, allowing for idiomatic Clojure when working with Rasdaman.


## Dependencies [&#x219F;](#contents)

 * A running instance of Rasdaman
 * PostgreSQL and postgis
 * The Rasdaman PostgreSQL backend configured to accept network connections
 * ``lein``


## Usage [&#x219F;](#contents)


### Connecting

```clojure
=> (require '[reggae.core :as reggae])
nil
=> (def client (reggae/make-client :host "172.16.0.42"))
#'client
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

TBD
