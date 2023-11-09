package com.unilim.iut.truckers.command

import android.content.Context
import com.unilim.iut.truckers.controller.JsonController

abstract class Command() {

    abstract var context: Context?
    abstract var donnee: Any?

    companion object {
        val jsonController: JsonController = JsonController()
    }

    constructor(context: Context, donnee: Any?): this() {
        this.context = context
        this.donnee = donnee
    }

    abstract fun executer(): Boolean
}