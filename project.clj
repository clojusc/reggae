(defn get-banner
  []
  (try
    (str
      (slurp "resources/text/banner.txt")
      (slurp "resources/text/loading.txt"))
    ;; If another project can't find the banner, just skip it;
    ;; this function is really only meant to be used by Dragon itself.
    (catch Exception _ "")))

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject clojusc/reggae "0.1.3-SNAPSHOT"
  :description "A Clojure wrapper for the Rasdaman Java Client Library"
  :url "https://github.com/clojusc/reggae"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :exclusions [org.clojure/clojure]
  :dependencies [
    [clojusc/twig "0.3.2-SNAPSHOT"]
    [dire "0.5.4"]
    [odmg/odmg "3.0"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/tools.namespace "0.2.11"]
    [org.rasdaman/rasj "9.0.4"]]
  :profiles {
    :uber {
      :aot :all}
    :test {
      :exclusions [org.clojure/clojure]
      :plugins [
        [jonase/eastwood "0.2.4"]
        [lein-ancient "0.6.12"]
        [lein-kibit "0.1.5"]
        [lein-shell "0.5.0"]]
      :test-selectors {
        :default :unit
        :unit :unit
        :system :system
        :integration :integration}}
    :dev {
      :exclusions [org.clojure/clojure]
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"]]
      :source-paths ["dev-resources/src"]
      :repl-options {
        :init-ns reggae.dev
        :prompt ~get-prompt
        :init ~(println (get-banner))}}}
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
      ["check"]
      ;["check-deps"]
      ;["lint"]
      ["test"]
      ["compile"]
      ["uberjar"]]})
