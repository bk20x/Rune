(asdf:defsystem "reach"
  :depends-on ("cl-json")
  :components ((:file "trades")
               (:file "reach" :depends-on "trades")))
