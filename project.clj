(defproject clojusc/reggae "0.1.1-SNAPSHOT"
  :description "A Clojure wrapper for the Rasdaman Java Client Library"
  :url "https://github.com/clojusc/reggae"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:init-ns reggae.dev}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.rasdaman/rasj "9.0.4"]
                 [dire "0.5.4"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [clojusc/twig "0.2.1"]]
  :profiles {
    :dev {
      :dependencies [[org.clojure/tools.namespace "0.2.11"]
                     [slamhound "1.5.5"]]
      :plugins [[lein-kibit "0.1.2"]
                [jonase/eastwood "0.2.3"]]
      :aliases {"slamhound" ["run" "-m" "slam.hound"]}
      :source-paths ["dev-resources/src"]}})
