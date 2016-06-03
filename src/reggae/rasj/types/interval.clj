(ns reggae.rasj.types.interval
  (:require [clojure.tools.logging :as log])
  (:import [rasj RasMInterval])
  (:refer-clojure :exclude [nth]))

(defprotocol ReggaeInterval
  "Represents a Reggae (Rasdaman) interval."
  (intersects? [this interval]
    "Determines if the this object intersects with the passed interval.")
  (nth [this index]
    "")
  (set! [this index value]
    "")
  (equal? [this interval]
    "")
  (not-equal? [this interval]
    "")
  (dim [this]
    "")
  (get-origin [this]
    "")
  (get-extent [this]
    "Gets the size of this interval as a point, that means the point specifies
    the extent of this MInterval ( i.e. high() - low() ) in each dimension.")
  (->str [this]
    "Return a string representation of the ``ReggaeBag``."))

(def reggae-interval-behaviour
  {:intersects?
    (fn [this interval]
      (.intersectsWith this interval))
   :nth
    (fn [this index]
      (.item this keep-indexed))
   :set!
    (fn [this index value]
      (.setItem this index value))
   :equal?
    (fn [this interval]
      (.equals this interval))
   :not-equal?
    (fn [this interval]
      (.notEquals this interval))
   :dim
    (fn [this]
      (.dimension this))
   :get-origin
    (fn [this]
      (.getOrigin this))
   :get-extent
    (fn [this]
      (.getExtent this))
   :->str
    (fn [this]
      (.toString this))})

(extend RasMInterval ReggaeInterval reggae-interval-behaviour)
