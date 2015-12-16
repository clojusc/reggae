(ns reggae.dev
  (:require [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [reggae.core :as reggae]))

(def reload #'repl/refresh)

;;; Aliases
(def new #'reggae/make-client)
(def q #'reggae/query)
