(defproject datomic-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.datomic/datomic-free "0.9.4572"]
                 [expectations "2.0.4"]]
  :plugins [[lein-autoexpect "1.2.2"]
            [lein-datomic "0.2.0"]]
  :datomic {:schemas ["resources/datomic" ["schema.edn"]]}
  :profiles {:dev
             {:datomic {:config "resources/datomic/free-transactor-template.properties"
                        :db-uri "datomic:free://localhost:4334/users-dbtest"}}})
