data class PlantStatus(

    /** DC input power in kW */
    val pvOutputPower: Double,

    /** AC active power in kW */
    val loadPower: Double,

    val feedInPower: Double,

    val batteryPower: Double,
    val batteryStateOfCharge: Double
) {

    fun toPrettyString(): String = """
            ### Plant status
            PV output power: $pvOutputPower kW
            Load power: $loadPower kW
            Feed-in power: $feedInPower kW
            Battery charge/discharge power: $batteryPower kW
            Battery SOC: $batteryStateOfCharge %
        """.trimIndent()
}