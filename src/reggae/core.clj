(ns reggae.core
  (:import [rasj RasImplementation]
           [org.odmg Database]))

(defn get-client-template [scheme host port dbname mode]
  {:scheme scheme
   :host host
   :port port
   :dbname dbname
   :mode mode
   :client nil
   :conn nil})

(defn get-mode [mode]
  (case mode
    :read-only Database/OPEN_READ_ONLY
    :read-write Database/OPEN_READ_WRITE
    :exclusive Database/OPEN_EXCLUSIVE))

(defn new-client [scheme host port]
  (new RasImplementation
       (format "%s://%s:%s" scheme host port)))

(defn new-connection [client-obj dbname mode]
  (let [db (.newDatabase client-obj)]
    (.open db dbname (get-mode mode))
    db))

(defn make-client [& {:keys [scheme host port dbname mode]
                      :or {scheme :http
                           host "127.0.0.1"
                           port 7001
                           dbname "RASBASE"
                           mode :read-only}}]
  (let [client (get-client-template scheme host port dbname mode)
        client-obj (new-client scheme host port)
        conn-obj (new-connection client-obj dbname mode)]
    (-> client
        (assoc :client client-obj)
        (assoc :conn conn-obj))))

(defn- -get-conn [client dbname mode]
  (if (:conn client)
    client
    (assoc client :conn (new-connection (:client client) dbname mode))))

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

(defn- -run-query [client-obj query-str]
  (-> (.newOQLQuery client-obj)
      (#(do (.create % query-str) %))
      (.execute)))

(defn query [client query-str]
  (let [client-obj (:client client)
        tx (.newTransaction client-obj)]
    (.begin tx)
    (let [results (-run-query client-obj query-str)]
      (.commit tx)
      results)))
