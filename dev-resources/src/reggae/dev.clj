(ns reggae.dev
  (:require [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [clojusc.twig :as logger]
            [reggae.core :as reggae]
            [reggae.interval :as interval]
            [reggae.query :as query]
            [reggae.rasj.types.interval :as rinterval]
            [reggae.rasj.types.point :as rpoint]))

(logger/set-level! '[reggae] :debug)

(def reload #'repl/refresh)

;;; Aliases
(def new #'reggae/make-client)
(def q #'reggae/query)
