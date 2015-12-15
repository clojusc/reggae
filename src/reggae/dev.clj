(ns reggae.dev
  (:require [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as repl]
            [reggae.core :refer :all]))

(def reload #'repl/refresh)
