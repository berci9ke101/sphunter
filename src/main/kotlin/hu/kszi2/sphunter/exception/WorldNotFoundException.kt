package hu.kszi2.sphunter.exception

class WorldNotFoundException : SPHunterException {
    constructor() : super()
    constructor(message: String) : super(message)
}