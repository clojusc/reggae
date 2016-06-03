(defproject reggae "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:init-ns reggae.dev}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [org.rasdaman/rasj "9.0.4"]
                 [dire "0.5.3"]
                 [org.clojure/tools.namespace "0.2.11"]]
  :profiles {
    :dev {
      :dependencies [[org.clojure/tools.namespace "0.2.11"]
                     [slamhound "1.5.5"]]
      :aliases {"slamhound" ["run" "-m" "slam.hound"]}
      :source-paths ["dev-resources/src"]}})
