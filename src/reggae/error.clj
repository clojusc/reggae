(ns reggae.error)

(defprotocol ThrowableProtocol
  "A Clojure wrapper for Throwable."
  (get-cause [this]
    "Returns the cause of this throwable or `nil` if the cause is nonexistent or unknown.")
  (get-localized-message [this]
    "")
  (get-message [this]
    "")
  (get-stack-trace [this]
    "")
  (->str [this]
    ""))

(def throwable-behaviour
  {:get-cause
    (fn [this] (.getCause this))
   :get-localized-message
    (fn [this] (.getLocalizedMessage this))
   :get-message
    (fn [this] (.getMessage this))
   :get-stack-trace
    (fn [this] (.getStackTrace this))
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
