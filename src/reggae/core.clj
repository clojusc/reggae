(ns reggae.core
  (:import [rasj RasImplementation]
           [org.odmg Database]))

(def open-read-only Database/OPEN_READ_ONLY)
(def open-read-write Database/OPEN_READ_WRITE)
(def open-exclusive Database/OPEN_EXCLUSIVE)

(defn make-client [& {:keys [scheme host port]
                      :or {scheme :http host "127.0.0.1" port 9418}
                      :as args}]
  (RasImplementation. (format "%s://%s:%s" scheme host port)))

(defn conn [client & {:keys [dbname mode]
                      :or {dbname "rasdaman" mode open-read-only}}]
  (let [db (.newDatabase client)]
    (.open db dbname mode)
    db))

