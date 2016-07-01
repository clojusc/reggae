(ns reggae.error)

(defprotocol ThrowableProtocol
  "A Clojure wrapper for Throwable."
  (get-cause [this]
    (str "Returns the cause of this throwable or `nil` if the cause is "
         "nonexistent or unknown."))
  (get-localized-message [this]
    "Creates a localized description of this throwable.")
  (get-message [this]
    "Returns the detail message string of this throwable.")
  (get-stack-trace [this]
    (str "Provides programmatic access to the stack trace information "
         "printed by ``print-stack-trace``."))
  (print-stack-trace [this]
    "Prints this throwable and its backtrace to the standard error stream.")
  (->str [this]
    "Returns a short description of this throwable."))

(def throwable-behaviour
  {:get-cause
    (fn [this] (.getCause this))
   :get-localized-message
    (fn [this] (.getLocalizedMessage this))
   :get-message
    (fn [this] (.getMessage this))
   :get-stack-trace
    (fn [this] (.getStackTrace this))
   :print-stack-trace
    (fn [this] (.printStackTrace this))
   :->str
    (fn [this] (.toString this))})

(extend java.lang.Throwable ThrowableProtocol throwable-behaviour)

(defn query-data
  "Get the data for a query error."
  [err client-data query-str]
  {:error err
   :message (get-message err)
   :client client-data
   :query query-str})

(defn new-connection
  "Get the data for a DB connection error."
  [err db-name mode]
  {:error err
   :message (get-message err)
   :database db-name
   :mode mode})
