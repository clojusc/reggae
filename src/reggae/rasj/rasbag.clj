(ns reggae.rasj.rasbag
  (:require [clojure.tools.logging :as log])
  (:import [rasj.odmg RasBag]
           [org.odmg DBag]))

(defn rasbag?
  ""
  [obj]
  (= (type obj) RasBag))

(defprotocol ReggaeBag
  "Represents a Reggae (Rasdaman) bag collection.

  This protocol
  * wraps the ``rasj.odmg.RasBag`` class
    * which extends the ``rasj.odmg.RasCollection`` class
  * and implements the ``org.odmg.DBag`` interface.

  The DBag interface defines the operations associated with an ODMG bag
  collection. All of the operations defined by the JavaSoft ``Collection``
  interface are supported by an ODMG implementation of ``DBag``, the exception
  ``UnsupportedOperationException`` is not thrown when a call is made to any of
  the ``Collection`` methods. "
  (add [this obj]
    "Appends the specified element to this ``ReggaeBag``.")
  (get-occurrences [this obj]
    "Returns the number of occurrences of the object obj in the ``ReggaeBag``
    collection.")
  (difference [this bag]
    "Creates a new ``ReggaeBag`` instance that contains the difference of this
    object and the ``ReggaeBag`` instance referenced by the passed ``ReggaeBag``.")
  (intersection [this bag]
    "Creates a new ``ReggaeBag`` instance that contains the intersection of this
    object and the ``ReggaeBag`` referenced by the passed ``ReggaeBag``.")
  (union [this bag]
    "Creates a new ``ReggaeBag`` instance that is the union of this object and the
    pass ``ReggaeBag``.")
  (get-iterator [this]
    "Returns an iterator over the elements in this ``ReggaeBag`` in proper
    sequence.")
  (size [this]
    "Returns the number of elements in this ``ReggaeBag``.")
  (->str [this]
    "Return a string representation of the ``ReggaeBag``."))

(def reggae-bag-behaviour
  {:add
    (fn [this obj]
      (.add this obj))
   :get-occurrences
    (fn [this obj]
      (.occurrences this obj))
   :difference
    (fn [this bag]
      (.difference this bag))
   :intersection
    (fn [this bag]
      (.intersection this bag))
   :union
    (fn [this bag]
      (.union this bag))
   :get-iterator
    (fn [this]
      (.iterator this))
   :size
    (fn [this]
      (.size this))
   :->str
    (fn [this]
      (.toString this))})

(extend RasBag ReggaeBag reggae-bag-behaviour)
