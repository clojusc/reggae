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

TBD


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
