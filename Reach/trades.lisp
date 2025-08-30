(defpackage :trades
  (:use :cl :cl-json)
  (:export #:trade #:items #:item #:parse-trade #:read-file-as-string))

(in-package :trades)


(defun read-file-as-string (file)
  (with-open-file (stream file :direction :input
                               :element-type 'character)
    (let ((contents (make-string (file-length stream))))
      (read-sequence contents stream)
        contents)))

(defclass trade ()
  ((from
    :initarg :from
    :accessor from)
   (timestamp
    :initarg :timestamp
    :accessor timestamp)
   (offer
    :initarg :offer
    :accessor offer)
   (items
    :initarg :items
    :accessor items)))

(defclass item ()
  ((name
    :initarg :name
    :accessor name)
   (amount
    :initarg :amount
    :accessor amount)))


(defun parse-trade (json)
  (cl-json:with-decoder-simple-clos-semantics
    (cl-json:decode-json-from-string json)))

