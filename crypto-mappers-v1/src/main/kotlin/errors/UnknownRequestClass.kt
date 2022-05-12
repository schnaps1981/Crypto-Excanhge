package errors

class UnknownRequestClass(clazz: Class<*>, clazzContext: Class<*>) :
    RuntimeException("$clazz cannot be mapped to $clazzContext")
