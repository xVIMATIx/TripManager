package com.example.tripmanager.data.model

data class Country(
    val name: String,
    val code: String,
    val cities: List<City>
)

data class City(
    val name: String,
    val state: String? = null,
    val timeZone: String
)

object Countries {
    val COUNTRIES = listOf(
        Country(
            name = "México",
            code = "MX",
            cities = listOf(
                City("Ciudad de México", null, "America/Mexico_City"),
                City("Cancún", "Quintana Roo", "America/Cancun"),
                City("Monterrey", "Nuevo León", "America/Monterrey"),
                City("Guadalajara", "Jalisco", "America/Mexico_City"),
                City("Tijuana", "Baja California", "America/Tijuana"),
                City("Mérida", "Yucatán", "America/Merida"),
                City("Chihuahua", "Chihuahua", "America/Chihuahua"),
                City("Hermosillo", "Sonora", "America/Hermosillo"),
                City("Mazatlán", "Sinaloa", "America/Mazatlan"),
                City("Oaxaca", "Oaxaca", "America/Mexico_City"),
                City("Puebla", "Puebla", "America/Mexico_City"),
                City("Querétaro", "Querétaro", "America/Mexico_City"),
                City("Toluca", "Estado de México", "America/Mexico_City"),
                City("León", "Guanajuato", "America/Mexico_City"),
                City("Aguascalientes", "Aguascalientes", "America/Mexico_City")
            )
        ),
        Country(
            name = "Estados Unidos",
            code = "US",
            cities = listOf(
                City("Nueva York", "Nueva York", "America/New_York"),
                City("Los Ángeles", "California", "America/Los_Angeles"),
                City("Chicago", "Illinois", "America/Chicago"),
                City("Houston", "Texas", "America/Chicago"),
                City("Phoenix", "Arizona", "America/Phoenix"),
                City("Miami", "Florida", "America/New_York"),
                City("Seattle", "Washington", "America/Los_Angeles"),
                City("Boston", "Massachusetts", "America/New_York"),
                City("Denver", "Colorado", "America/Denver"),
                City("Las Vegas", "Nevada", "America/Los_Angeles"),
                City("San Francisco", "California", "America/Los_Angeles"),
                City("Washington", "D.C.", "America/New_York"),
                City("Atlanta", "Georgia", "America/New_York"),
                City("Dallas", "Texas", "America/Chicago"),
                City("Detroit", "Michigan", "America/Detroit")
            )
        ),
        Country(
            name = "España",
            code = "ES",
            cities = listOf(
                City("Madrid", null, "Europe/Madrid"),
                City("Barcelona", "Cataluña", "Europe/Madrid"),
                City("Valencia", "Valencia", "Europe/Madrid"),
                City("Sevilla", "Andalucía", "Europe/Madrid"),
                City("Bilbao", "País Vasco", "Europe/Madrid"),
                City("Málaga", "Andalucía", "Europe/Madrid"),
                City("Zaragoza", "Aragón", "Europe/Madrid"),
                City("Murcia", "Murcia", "Europe/Madrid"),
                City("Palma", "Islas Baleares", "Europe/Madrid"),
                City("Las Palmas", "Canarias", "Atlantic/Canary")
            )
        ),
        Country(
            name = "Reino Unido",
            code = "GB",
            cities = listOf(
                City("Londres", "Inglaterra", "Europe/London"),
                City("Manchester", "Inglaterra", "Europe/London"),
                City("Birmingham", "Inglaterra", "Europe/London"),
                City("Liverpool", "Inglaterra", "Europe/London"),
                City("Edimburgo", "Escocia", "Europe/London"),
                City("Glasgow", "Escocia", "Europe/London"),
                City("Belfast", "Irlanda del Norte", "Europe/London"),
                City("Cardiff", "Gales", "Europe/London")
            )
        ),
        Country(
            name = "Francia",
            code = "FR",
            cities = listOf(
                City("París", "Île-de-France", "Europe/Paris"),
                City("Lyon", "Auvergne-Rhône-Alpes", "Europe/Paris"),
                City("Marsella", "Provence-Alpes-Côte d'Azur", "Europe/Paris"),
                City("Toulouse", "Occitania", "Europe/Paris"),
                City("Niza", "Provence-Alpes-Côte d'Azur", "Europe/Paris"),
                City("Nantes", "Pays de la Loire", "Europe/Paris"),
                City("Estrasburgo", "Gran Este", "Europe/Paris"),
                City("Burdeos", "Nueva Aquitania", "Europe/Paris")
            )
        ),
        Country(
            name = "Italia",
            code = "IT",
            cities = listOf(
                City("Roma", "Lacio", "Europe/Rome"),
                City("Milán", "Lombardía", "Europe/Rome"),
                City("Nápoles", "Campania", "Europe/Rome"),
                City("Turín", "Piamonte", "Europe/Rome"),
                City("Palermo", "Sicilia", "Europe/Rome"),
                City("Florencia", "Toscana", "Europe/Rome"),
                City("Venecia", "Véneto", "Europe/Rome"),
                City("Génova", "Liguria", "Europe/Rome")
            )
        ),
        Country(
            name = "Alemania",
            code = "DE",
            cities = listOf(
                City("Berlín", null, "Europe/Berlin"),
                City("Múnich", "Baviera", "Europe/Berlin"),
                City("Hamburgo", null, "Europe/Berlin"),
                City("Fráncfort", "Hesse", "Europe/Berlin"),
                City("Colonia", "Renania del Norte-Westfalia", "Europe/Berlin"),
                City("Stuttgart", "Baden-Wurtemberg", "Europe/Berlin"),
                City("Düsseldorf", "Renania del Norte-Westfalia", "Europe/Berlin"),
                City("Dortmund", "Renania del Norte-Westfalia", "Europe/Berlin")
            )
        ),
        Country(
            name = "Japón",
            code = "JP",
            cities = listOf(
                City("Tokio", null, "Asia/Tokyo"),
                City("Osaka", null, "Asia/Tokyo"),
                City("Kioto", null, "Asia/Tokyo"),
                City("Yokohama", null, "Asia/Tokyo"),
                City("Sapporo", "Hokkaido", "Asia/Tokyo"),
                City("Nagoya", null, "Asia/Tokyo"),
                City("Fukuoka", null, "Asia/Tokyo"),
                City("Hiroshima", null, "Asia/Tokyo")
            )
        ),
        Country(
            name = "China",
            code = "CN",
            cities = listOf(
                City("Pekín", null, "Asia/Shanghai"),
                City("Shanghái", null, "Asia/Shanghai"),
                City("Hong Kong", null, "Asia/Hong_Kong"),
                City("Cantón", null, "Asia/Shanghai"),
                City("Shenzhen", null, "Asia/Shanghai"),
                City("Chengdu", null, "Asia/Shanghai"),
                City("Xi'an", null, "Asia/Shanghai"),
                City("Hangzhou", null, "Asia/Shanghai")
            )
        ),
        Country(
            name = "Argentina",
            code = "AR",
            cities = listOf(
                City("Buenos Aires", null, "America/Argentina/Buenos_Aires"),
                City("Córdoba", "Córdoba", "America/Argentina/Cordoba"),
                City("Rosario", "Santa Fe", "America/Argentina/Buenos_Aires"),
                City("Mendoza", "Mendoza", "America/Argentina/Mendoza"),
                City("La Plata", "Buenos Aires", "America/Argentina/Buenos_Aires"),
                City("Tucumán", "Tucumán", "America/Argentina/Tucuman"),
                City("Mar del Plata", "Buenos Aires", "America/Argentina/Buenos_Aires"),
                City("Salta", "Salta", "America/Argentina/Salta")
            )
        ),
        Country(
            name = "Brasil",
            code = "BR",
            cities = listOf(
                City("São Paulo", "São Paulo", "America/Sao_Paulo"),
                City("Río de Janeiro", "Río de Janeiro", "America/Sao_Paulo"),
                City("Brasilia", "Distrito Federal", "America/Sao_Paulo"),
                City("Salvador", "Bahía", "America/Bahia"),
                City("Fortaleza", "Ceará", "America/Fortaleza"),
                City("Belo Horizonte", "Minas Gerais", "America/Sao_Paulo"),
                City("Manaus", "Amazonas", "America/Manaus"),
                City("Curitiba", "Paraná", "America/Sao_Paulo"),
                City("Recife", "Pernambuco", "America/Recife"),
                City("Porto Alegre", "Río Grande del Sur", "America/Sao_Paulo")
            )
        ),
        Country(
            name = "Colombia",
            code = "CO",
            cities = listOf(
                City("Bogotá", "Cundinamarca", "America/Bogota"),
                City("Medellín", "Antioquia", "America/Bogota"),
                City("Cali", "Valle del Cauca", "America/Bogota"),
                City("Barranquilla", "Atlántico", "America/Bogota"),
                City("Cartagena", "Bolívar", "America/Bogota"),
                City("Bucaramanga", "Santander", "America/Bogota"),
                City("Pereira", "Risaralda", "America/Bogota"),
                City("Santa Marta", "Magdalena", "America/Bogota")
            )
        ),
        Country(
            name = "Chile",
            code = "CL",
            cities = listOf(
                City("Santiago", "Región Metropolitana", "America/Santiago"),
                City("Valparaíso", "Valparaíso", "America/Santiago"),
                City("Concepción", "Biobío", "America/Santiago"),
                City("La Serena", "Coquimbo", "America/Santiago"),
                City("Antofagasta", "Antofagasta", "America/Santiago"),
                City("Puerto Montt", "Los Lagos", "America/Santiago"),
                City("Iquique", "Tarapacá", "America/Santiago"),
                City("Temuco", "Araucanía", "America/Santiago")
            )
        ),
        Country(
            name = "Perú",
            code = "PE",
            cities = listOf(
                City("Lima", null, "America/Lima"),
                City("Arequipa", "Arequipa", "America/Lima"),
                City("Cusco", "Cusco", "America/Lima"),
                City("Trujillo", "La Libertad", "America/Lima"),
                City("Chiclayo", "Lambayeque", "America/Lima"),
                City("Iquitos", "Loreto", "America/Lima"),
                City("Piura", "Piura", "America/Lima"),
                City("Huancayo", "Junín", "America/Lima")
            )
        ),
        Country(
            name = "Australia",
            code = "AU",
            cities = listOf(
                City("Sídney", "Nueva Gales del Sur", "Australia/Sydney"),
                City("Melbourne", "Victoria", "Australia/Melbourne"),
                City("Brisbane", "Queensland", "Australia/Brisbane"),
                City("Perth", "Australia Occidental", "Australia/Perth"),
                City("Adelaida", "Australia Meridional", "Australia/Adelaide"),
                City("Gold Coast", "Queensland", "Australia/Brisbane"),
                City("Canberra", "Territorio de la Capital", "Australia/Sydney"),
                City("Darwin", "Territorio del Norte", "Australia/Darwin")
            )
        ),
        Country(
            name = "Canadá",
            code = "CA",
            cities = listOf(
                City("Toronto", "Ontario", "America/Toronto"),
                City("Montreal", "Quebec", "America/Montreal"),
                City("Vancouver", "Columbia Británica", "America/Vancouver"),
                City("Calgary", "Alberta", "America/Edmonton"),
                City("Ottawa", "Ontario", "America/Toronto"),
                City("Edmonton", "Alberta", "America/Edmonton"),
                City("Winnipeg", "Manitoba", "America/Winnipeg"),
                City("Quebec", "Quebec", "America/Montreal")
            )
        )
    )
    
    fun getCountries(): List<String> = COUNTRIES.map { it.name }
    
    fun getCountryByName(name: String): Country? = COUNTRIES.find { it.name.equals(name, ignoreCase = true) }
    
    fun getCitiesByCountry(countryName: String): List<City> {
        return getCountryByName(countryName)?.cities ?: emptyList()
    }
}
