(ns reggae.db
  (:require [clojure.tools.logging :as log]
            [dire.core :refer [with-handler!]]
            [reggae.error :as rerror])
  (:import [rasj RasImplementation RasClientInternalException]
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

(defn new-connection [client-obj db-name mode]
  (log/debug "Creating database connection instance and opening it ...")
  (let [db (.newDatabase client-obj)]
    (.open db db-name (get-mode mode))
    db))

;;; Error handlers

(with-handler! #'new-connection
  RasClientInternalException
  (fn [e client-obj db-name mode]
    (log/error "Could not connect to Rasdaman.")
    (log/debug e)
    (rerror/new-connection e db-name mode)))

