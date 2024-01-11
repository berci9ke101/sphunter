package hu.kszi2.sphunter.exception

class ServerNotFoundException : SPHunterException {
    constructor() : super()
    constructor(message: String) : super(message)
}