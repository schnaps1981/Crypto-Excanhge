package errors

class UnknownCommand(command: String, clazz: Class<*>) :
    Throwable("Wrong command $command in $clazz context at mapping toTransport stage")
