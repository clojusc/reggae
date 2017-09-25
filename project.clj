(defproject clojusc/reggae "0.1.2-SNAPSHOT"
  :description "A Clojure wrapper for the Rasdaman Java Client Library"
  :url "https://github.com/clojusc/reggae"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repl-options {:init-ns reggae.dev}
  :dependencies [
    [clojusc/twig "0.2.6"]]
    [dire "0.5.4"]
    [odmg/odmg "3.0"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/tools.namespace "0.2.11"]
    [org.rasdaman/rasj "9.0.4"]
  :profiles {
    :uber {
      :aot :all}
    :test {
      :plugins [
        [jonase/eastwood "0.2.3" :exclusions [org.clojure/clojure]]
        [lein-kibit "0.1.2" :exclusions [org.clojure/clojure]]]
      :test-selectors {
        :default :unit
        :unit :unit
        :system :system
        :integration :integration}}
    :dev {
      :dependencies [[org.clojure/tools.namespace "0.2.11"]]
      :source-paths ["dev-resources/src"]}}
  :aliases {
    "check-deps" [
      "with-profile" "+test" "ancient" "check" ":all"]
    "kibit" [
      "with-profile" "+test" "do"
        ["shell" "echo" "== Kibit =="]
        ["kibit"]]
    "outlaw" [
      "with-profile" "+test"
      "eastwood" "{:namespaces [:source-paths] :source-paths [\"src\"]}"]
    "lint" [
      "with-profile" "+test" "do"
        ["check"] ["kibit"] ["outlaw"]]
    "build" ["with-profile" "+test" "do"
      ["check-deps"]
      ["lint"]
      ["test"]
      ["compile"]
      ["uberjar"]]})
