import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster

const val DELAY_BETWEEN_POLLS_MS: Long = 3000

const val DEFAULT_DEVICE_ID: Int = 0
const val DEFAULT_MODBUS_PORT: Int = 502

fun main() {

    pollModbus(
        ipAddress = "192.168.0.100",
        responseHandler = { plantStatus ->

            println(plantStatus.toPrettyString())
        }
    )
}

private fun pollModbus(
    ipAddress: String,
    port: Int = DEFAULT_MODBUS_PORT,
    responseHandler: (PlantStatus) -> Unit
) {

    while (true) {

        val master = ModbusTCPMaster(ipAddress, port);

        master.connect()

        while (master.isConnected) {

            val inputPowerAnswer = master.readMultipleRegisters(DEFAULT_DEVICE_ID,30354, 8)

            val batteryAnswer = master.readMultipleRegisters(DEFAULT_DEVICE_ID, 30368, 1)

            /* Convert the answers */

            // FIXME Should be unsigned
            val pvOutputPower = convertToInt32(
                highWord = inputPowerAnswer[0].value,
                lowWord = inputPowerAnswer[1].value
            ) / 1000.0

            // FIXME Should be unsigned
            val loadPower = convertToInt32(
                highWord = inputPowerAnswer[2].value,
                lowWord = inputPowerAnswer[3].value
            ) / 1000.0

            val feedInPower = convertToInt32(
                highWord = inputPowerAnswer[4].value,
                lowWord = inputPowerAnswer[5].value
            ) / 1000.0

            val batteryPower = convertToInt32(
                highWord = inputPowerAnswer[6].value,
                lowWord = inputPowerAnswer[7].value
            ) / 1000.0

            val batteryStateOfCharge = batteryAnswer[0].value / 100.0

            /* Create data object */

            val plantStatus = PlantStatus(
                pvOutputPower = pvOutputPower,
                loadPower = loadPower,
                feedInPower = feedInPower,
                batteryPower = batteryPower,
                batteryStateOfCharge = batteryStateOfCharge
            )

            responseHandler(plantStatus)

            Thread.sleep(DELAY_BETWEEN_POLLS_MS)
        }

        /* Wait until new connect attempt. */
        Thread.sleep(DELAY_BETWEEN_POLLS_MS)
    }
}

private fun convertToInt32(
    highWord: Int,
    lowWord: Int
): Int = (highWord shl 16) or (lowWord and 0xFFFF)
