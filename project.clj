(defproject calc-clj/calc-clj "0.1.0-SNAPSHOT"
  :description "4+ function RPN calculator for Android."
  :url "http://github.com/warreq/calc-clj"
  :license {:name "UNLICENSE"
            :url "https://unlicense.org/UNLICENSE"}

  :global-vars {*warn-on-reflection* true}

  :source-paths ["src/clojure" "src"]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :plugins [[lein-droid "0.4.3"]]

  :dependencies [[org.clojure-android/clojure "1.7.0-r2"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [neko/neko "4.0.0-alpha5"]
                 [me.grantland/autofittextview "0.2.1" :extension "aar"]]
  :profiles {:default [:dev]

             :dev
             [:android-common :android-user
              {:dependencies [[org.clojure/tools.nrepl "0.2.10"]]
               :target-path "target/debug"
               :android {:aot :all-with-unused
                         :rename-manifest-package "warreq.kea.calc.debug"
                         :manifest-options {:app-name "Cult Calc (debug)"}}}]
             :release
             [:android-common
              {:target-path "target/release"
               :android
               {;; :keystore-path "/home/user/.android/private.keystore"
                ;; :key-alias "mykeyalias"
                ;; :sigalg "MD5withRSA"

                :ignore-log-priority [:debug :verbose]
                :aot :all
                :build-type :release}}]

             :lean
             [:release
              {:dependencies ^:replace [[org.skummet/clojure "1.7.0-r1"]
                                        [org.clojure/math.numeric-tower "0.0.4"]
                                        [neko/neko "4.0.0-alpha5"]
                                        [me.grantland/autofittextview "0.2.1" :extension "aar"]]
               :exclusions [[org.clojure/clojure]
                            [org.clojure-android/clojure]]
               :jvm-opts ["-Dclojure.compile.ignore-lean-classes=true"]
               :global-vars ^:replace {clojure.core/*warn-on-reflection* true}
               :android {:lean-compile true
                         :proguard-execute false
                         :proguard-conf-path "build/proguard-minify.cfg"}}]}

  :android {;; Specify the path to the Android SDK directory.
            ;; :sdk-path "/home/user/path/to/android-sdk/"

            ;; Try increasing this value if dexer fails with
            ;; OutOfMemoryException. Set the value according to your
            ;; available RAM.
            :dex-opts ["-JXmx4096M" "--incremental"]

            :target-version "15"
            :aot-exclude-ns ["clojure.parallel" "clojure.core.reducers"
                             "cider.nrepl" "cider-nrepl.plugin"
                             "cider.nrepl.middleware.util.java.parser"
                             #"cljs-tooling\..+"]})
