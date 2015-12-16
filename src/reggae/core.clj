(ns reggae.core
  (:require [reggae.db :as db]))

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
        client-obj (db/new-client scheme host port)
        conn-obj (db/new-connection client-obj dbname mode)]
    (-> client
        (assoc :client client-obj)
        (assoc :conn conn-obj))))

(defn- -get-conn [client dbname mode]
  (if (:conn client)
    client
    (assoc client :conn (db/new-connection (:client client) dbname mode))))

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

(defn query [client query-str]
  (let [client-obj (:client client)
        tx (.newTransaction client-obj)]
    ;; XXX add with or try/finally form here, to avoid orphanned tx
    (.begin tx)
    (let [results (db/run-query client-obj query-str)]
      (.commit tx)
      results)))
