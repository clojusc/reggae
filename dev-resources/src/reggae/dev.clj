(ns reggae.dev
  (:require [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [clojusc.twig :as logger]
            [reggae.core :as reggae]))

(logger/set-level! '[reggae] :debug)

(def reload #'repl/refresh)

;;; Aliases
(def new #'reggae/make-client)
(def q #'reggae/query)
