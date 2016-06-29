(ns reggae.rasj.rasstruct
  (:require [clojure.tools.logging :as log])
  (:import [rasj RasStructure RasStructureType])
  (:refer-clojure :exclude [get]))

(defprotocol ReggaeStruct
  "Represents a Reggae (Rasdaman) struct."
  (get-type [this]
    "")
  (get-elements [this]
    ""))

(def reggae-struct-behaviour
  {:get-type
    (fn [this]
      (.getType this))
   :get-elements
    (fn [this]
      (.getElements this))})

(extend RasStructure ReggaeStruct reggae-struct-behaviour)

(defprotocol ReggaeStructType
  "Represents a Reggae (Rasdaman) struct type."
  (get-type-id [this]
    "")
  (get-base-types [this]
    "")
  (get-attributes [this]
    "")
  (struct? [this]
    "")
  (equal? [this]
    "")
  (->vector [this]
    ""))

(def reggae-structtype-behaviour
  {:get-type-id
    (fn [this]
      (.getTypeID this))
   :get-base-types
    (fn [this]
      (.getBaseTypes this))
   :get-attributes
    (fn [this]
      (.getAttributes this))
   :struct?
    (fn [this]
      (.isStructType this))
   :equal?
    (fn [this obj]
      (.equals this obj))})

(extend RasStructureType ReggaeStructType reggae-structtype-behaviour)

(defn ->str
  "Return a string representation of the given object."
  [this]
  (str this))
