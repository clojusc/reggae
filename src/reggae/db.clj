(ns reggae.db
  (:require [clojure.tools.logging :as log])
  (:import [rasj RasImplementation]
           [org.odmg Database]))

(defn get-mode [mode]
  (case mode
    :read-only Database/OPEN_READ_ONLY
    :read-write Database/OPEN_READ_WRITE
    :exclusive Database/OPEN_EXCLUSIVE))

(defn new-client [scheme host port]
  (log/debugf "Creating new client with %s://%s:%s ..." scheme host port)
  (new RasImplementation
       (format "%s://%s:%s" scheme host port)))

(defn new-connection [client-obj dbname mode]
  (log/debug "Creating database connection instance and opening it ...")
  (let [db (.newDatabase client-obj)]
    (.open db dbname (get-mode mode))
    db))

(defn run-query [client-obj query-str]
  (log/debugf "Running query '%s'..." query-str)
  (-> (.newOQLQuery client-obj)
      (#(do (.create % query-str) %))
      (.execute)))
