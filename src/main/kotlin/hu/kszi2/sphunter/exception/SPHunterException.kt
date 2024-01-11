package hu.kszi2.sphunter.exception

abstract class SPHunterException : RuntimeException {
    companion object {
        private const val prefix = "[SPHunter]:"
    }

    constructor() : super("$prefix Unknown error occurred!")
    constructor(message: String) : super("$prefix $message")
}