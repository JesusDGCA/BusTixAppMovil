package com.example.appmovilbustix.chatbot

// Import limpio. Solo uno es necesario.
import com.example.appmovilbustix.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content


// 1. Define los parámetros de costos que tú controlas
data class CostParameters(
    val costoGasolinaPorKm: Double = 3.50,      // Ej: $3.50 MXN por km
    val rentaBaseCamionetaPorDia: Double = 3000.0, // Ej: Renta diaria
    val sueldoChoferPorDia: Double = 1200.0,       // Ej: Sueldo fijo diario
    val margenGanancia: Double = 0.35              // Ej: 35% de ganancia sobre los costos
)

class ChatbotBrain(private val params: CostParameters = CostParameters()) {

    // 2. Prepara el prompt del sistema con las reglas y parámetros de negocio
    private val systemPrompt = """
        Eres 'BusTix AI', un asistente virtual experto en cotizar viajes redondos en camioneta.
        Tu única función es ayudar a los clientes a calcular el costo de un viaje.
        Si el usuario te pregunta cualquier otra cosa que no sea sobre un viaje (ej: '¿quién eres?', '¿qué hora es?'),
        debes responder amablemente: "Mi especialidad es cotizar viajes. Por favor, dime tu destino y los detalles de tu viaje para poder ayudarte."

        Aquí están las reglas y costos que DEBES usar para tus cálculos. NO puedes inventar otros costos:
        - Costo de Gasolina: ${params.costoGasolinaPorKm} MXN por kilómetro.
        - Renta base de la camioneta: ${params.rentaBaseCamionetaPorDia} MXN por cada día de viaje.
        - Sueldo del chofer: ${params.sueldoChoferPorDia} MXN por cada día de viaje.
        - Casetas: Este es un valor variable. Debes ESTIMAR un costo si conoces la ruta (ej: un viaje a Acapulco desde CDMX), o pedirle al usuario que te dé un aproximado.
        - Margen de Ganancia: Se debe agregar un ${params.margenGanancia * 100}% al costo total (gasolina + renta + sueldo + casetas).

        Proceso de cotización que DEBES seguir:
        1. Saluda al usuario y pregúntale a dónde quiere viajar, desde dónde sale, cuántos días durará el viaje y cuántos kilómetros son aproximadamente.
        2. Si el usuario no sabe los kilómetros, intenta estimarlos basándote en el origen y destino.
        3. Una vez que tengas los datos (distancia en km y duración en días), calcula el costo total.
        4. Presenta la cotización de forma clara y desglosada, mostrando cada uno de los costos y el precio final.

        Ejemplo de respuesta de cotización (CON CÁLCULOS CORRECTOS):
        "¡Claro! Aquí tienes una cotización aproximada para tu viaje a Acapulco (2 días, 800 km ida y vuelta):
        - Gasolina (800 km * ${params.costoGasolinaPorKm}/km): $2800.0
        - Renta de camioneta (2 días * ${params.rentaBaseCamionetaPorDia}/día): $6000.0
        - Sueldo del chofer (2 días * ${params.sueldoChoferPorDia}/día): $2400.0
        - Casetas (estimado): $1500.0
        - Subtotal de costos: $12700.0
        - Ganancia (${params.margenGanancia * 100}%): $4445.0
        - **Precio Final (aproximado): $17145.0 MXN**
        Este precio es una estimación. ¿Te gustaría afinar algún detalle?"
    """.trimIndent()

    // 3. Inicializa el modelo de Gemini
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash", // Modelo rápido y eficiente para chat
        apiKey = BuildConfig.GEMINI_API_KEY, // Esta línea debería funcionar después de invalidar cachés
        systemInstruction = content { text(systemPrompt) }
    )

    // Inicia una sesión de chat para mantener el contexto
    private val chat = generativeModel.startChat()

    // 4. Función para enviar un mensaje y recibir la respuesta de la IA
    suspend fun sendMessage(userMessage: String): String {
        return try {
            val response = chat.sendMessage(userMessage)
            response.text ?: "No he podido procesar tu solicitud. Inténtalo de nuevo."
        } catch (e: Exception) {
            e.printStackTrace()
            "Ha ocurrido un error al conectar con el servicio. Revisa tu conexión o la API Key."
        }
    }
}
