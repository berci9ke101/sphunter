package hu.kszi2.sphunter.exception

class PlayerNotFoundException : SPHunterException {
    constructor() : super()
    constructor(message: String) : super(message)
}