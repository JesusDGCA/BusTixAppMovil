package com.example.appmovilbustix.data

data class FaqItem(
    val question: String,
    val answer: String
)

fun getSampleFaqs(): List<FaqItem> {
    return listOf(
        FaqItem(
            "¿Cómo puedo cancelar un boleto?",
            "Para cancelar un boleto, ve a la sección 'Mis Boletos', selecciona el boleto que deseas cancelar y busca la opción de cancelación. Ten en cuenta que pueden aplicar políticas de cancelación dependiendo de la anticipación con la que lo hagas."
        ),
        FaqItem(
            "¿Qué pasa si pierdo mi autobús?",
            "Si pierdes tu autobús, lamentablemente el boleto no es reembolsable. Te recomendamos llegar al punto de partida con al menos 30 minutos de anticipación. En algunos casos, puedes contactar a soporte para ver si hay posibilidad de reasignarte a un viaje posterior, aunque esto no está garantizado."
        ),
        FaqItem(
            "¿Puedo cambiar mi asiento?",
            "Una vez comprado el boleto, no es posible cambiar el asiento a través de la aplicación. Si tienes alguna necesidad especial, por favor contacta a soporte para que puedan asistirte."
        ),
        FaqItem(
            "¿Qué objetos están prohibidos en el autobús?",
            "Está prohibido transportar armas, sustancias ilegales, materiales inflamables y animales (a excepción de perros de servicio certificados). El equipaje voluminoso debe ir en el compartimento de carga."
        ),
        FaqItem(
            "¿Cómo puedo facturar mi viaje?",
            "Después de completar tu viaje, recibirás un correo con un enlace para generar tu factura. También puedes solicitarla desde la sección 'Mis Boletos', en los detalles del boleto de un viaje ya realizado."
        )
    )
}
