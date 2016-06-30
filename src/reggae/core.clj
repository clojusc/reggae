(ns reggae.core
  (:require [clojure.tools.logging :as log]
            [dire.core :refer [with-handler!]]
            [reggae.db :as rdb]
            [reggae.query :as rq])
  (:import [org.odmg QueryException]))

(defn get-client-template [scheme host port dbname mode]
  {:scheme scheme
   :host host
   :port port
   :dbname dbname
   :mode mode
   :client nil
   :conn nil})

(defn make-client [& {:keys [scheme host port dbname mode]
                      :or {scheme :http
                           host "127.0.0.1"
                           port 7001
                           dbname "RASBASE"
                           mode :read-only}}]
  (let [client (get-client-template scheme host port dbname mode)
        client-obj (rdb/new-client scheme host port)
        conn-obj (rdb/new-connection client-obj dbname mode)]
    (-> client
        (assoc :client client-obj)
        (assoc :conn conn-obj))))

(defn- -get-conn [client dbname mode]
  (if (:conn client)
    client
    (assoc client :conn (rdb/new-connection (:client client) dbname mode))))

(defn get-conn [client & {:keys [dbname mode return-client]
                          :or {dbname (:dbname client)
                               mode (:mode client)}
                               return-client :false}]
  (let [updated (-get-conn client dbname mode)]
    (if return-client
      updated
      (:conn updated))))

(defn update-client [client & args]
  (apply assoc (into [client] args)))

(defn update-conn [client & {:keys [dbname mode]
                          :or {dbname (:dbname client)
                               mode (:mode client)}}]
  (-> client
      (update-client :conn nil :dbname dbname :mode mode)
      (get-conn :return-client true)))

(defn query
  ([client query-str]
    (let [client-obj (:client client)
          tx (.newTransaction client-obj)]
      (query client query-str client-obj tx)))
  ([client query-str client-obj tx]
    (log/debugf "Starting transation %s ..." tx)
    (.begin tx)
    (let [results (rq/run client-obj query-str)]
      (log/debugf "Committing transaction %s ..." tx)
      (.commit tx)
      results)))

;;; Exception handling

(with-handler! #'query
  QueryException
  (fn [e client query-str client-obj tx]
    (log/tracef "Got args: %s" [client query-str client-obj tx])
    (log/errorf "Error in query: %s" e)
    (log/debugf "Aborting transation %s ..." tx)
    (.abort tx)
    {:error e :message (.getMessage e) :client client :query query-str}))
