{
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 :default
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 {
  :use-pipeline         true
  :run-triggers         true
  :print
  {
   :action              false
   :actions             false
   :matchers            false
   :params              false
   :predicates          false
   :request             false
   :routes              false
   :triggers            false
  }
  :triggers
  {
   :thread-count        1
  }
 }

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 :development
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 {
  :http
  {
   :port                8082
  }
  :print
  {
   :action              true
   :actions             true
   :matchers            true
   :params              true
   :predicates          true
   :request             true
   :routes              true
   :triggers            true
  }
 }

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 :production
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 {
  :http
  {
   :port                8082
  }
  :print
  {
   :request             false
  }
 }

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 :test
 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 {
  :print
  {
   :actions             true
   :initializers        true
   :triggers            true
  }
 }
 
 }
