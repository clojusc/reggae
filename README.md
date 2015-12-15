# reggae

*A Clojure wrapper for the Rasdaman Java Client Library*

[![][logo]][logo-large]

[logo]: resources/images/clj-reggea-logo-3.png
[logo-large]: resources/images/clj-reggea-logo-3-large.png


##### Contents

* [Introduction](#introduction-)
* [Usage](#usage-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

The [Rasdaman site](http://www.rasdaman.org/) touts the database as the World's Leading Array Database, allowing users to store and query massive multi-dimensional ​arrays such as sensor, image, simulation, and statistics data appearing in domains like earth, space, and life science. During a review in Fall 2014, independent experts unanimously attested that, based on "proven evidence", rasdaman will "significantly transform the way scientists access and use data in a way that hitherto was not possible".

Rasdaman comes with client libraries written in C++ and Java. The Clojure reggae library is a thin wrapper around the Java library, allowing for idiomatic Clojure when working with Rasdaman.


## Usage [&#x219F;](#contents)

```clojure
=> (require '[reggae.core :as reggae])
=> (def client (reggae/make-client :host "172.16.0.42"))
#'client
=> (def conn (reggae/conn client :mode reggae/open-read-write))
#'conn
```


## License [&#x219F;](#contents)

TBD
