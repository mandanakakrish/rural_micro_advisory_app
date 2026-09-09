package com.ruraladvisory.advisory.model

/**
 * Entrepreneur profile representing the registered user, business parameters,
 * location, scale, and operational baseline.
 * Corresponds to Section 3 & 5 of the ABHYUDAY specification:
 * Pipeline: User -> Auth -> Profile -> Business Data -> AI Advisory
 */
data class UserProfile(
    val id: String = "new_user",
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "Male",
    val age: Int = 0,
    val state: String = "",
    val district: String = "",
    val block: String = "",
    val village: String = "",
    val pincode: String = "",
    val areaType: String = "Rural",
    val category: BusinessCategory = BusinessCategory.DAIRY,
    val customCategory: String = "",
    val scale: BusinessScale = BusinessScale.MICRO,
    val productionCapacity: String = "",
    val marginCapital: Double = 0.0,
    val monthlyRevenue: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val growthObjective: GrowthObjective = GrowthObjective.NEW_VENTURE,
    val businessDetails: String = "",
    val promoterCategory: String = "",
    val specialCategory: String = "None",
    val educationQualification: String = "",
    val hasEdpTraining: Boolean = false,
    val businessSubtype: String = "",
    val experienceYears: Int = 0
) {
    fun toLocationInfo(): LocationInfo = LocationInfo(
        village = village,
        block = block,
        district = district,
        state = state
    )

    fun toAdvisoryInput(language: LanguageCode = LanguageCode.EN): AdvisoryInput = AdvisoryInput(
        location = toLocationInfo(),
        marginCapital = marginCapital,
        category = category,
        language = language,
        customCategory = customCategory,
        businessDetails = businessDetails,
        growthObjective = growthObjective,
        scale = scale,
        productionCapacity = productionCapacity
    )

    companion object {
        val USER_A = UserProfile(
            id = "user_a_dairy",
            name = "Ramesh Patel",
            phoneNumber = "+91 98250 12345",
            email = "ramesh.patel@ruralbiz.in",
            gender = "Male",
            age = 38,
            state = "Gujarat",
            district = "Anand",
            block = "Petlad",
            village = "Nar",
            pincode = "388450",
            areaType = "Rural",
            category = BusinessCategory.DAIRY,
            scale = BusinessScale.SMALL,
            productionCapacity = "30 Cattle (400 L/Day)",
            marginCapital = 350000.0,
            monthlyRevenue = 180000.0,
            monthlyExpenses = 95000.0,
            growthObjective = GrowthObjective.EXPANSION,
            businessDetails = "Operating a dairy farm with 30 high-yield cows. Supplying milk to local cooperative union, looking to set up automated chilling and packaging.",
            promoterCategory = "OBC",
            educationQualification = "12th Pass",
            hasEdpTraining = true
        )

        val USER_B = UserProfile(
            id = "user_b_handicrafts",
            name = "Priya Sharma",
            phoneNumber = "+91 94140 67890",
            email = "priya.textiles@ruralcrafts.org",
            gender = "Female",
            age = 32,
            state = "Rajasthan",
            district = "Jaipur",
            block = "Sanganer",
            village = "Bagru",
            pincode = "303007",
            areaType = "Rural",
            category = BusinessCategory.HANDICRAFTS,
            scale = BusinessScale.MEDIUM,
            productionCapacity = "1,200 Craft Units/Mo",
            marginCapital = 650000.0,
            monthlyRevenue = 340000.0,
            monthlyExpenses = 175000.0,
            growthObjective = GrowthObjective.MARKET_REACH,
            businessDetails = "Handmade block printed textiles and pottery artisan collective employing 18 local women artisans, preparing for domestic exhibition and export channels.",
            promoterCategory = "General",
            specialCategory = "Women Entrepreneur",
            educationQualification = "Graduate",
            hasEdpTraining = true
        )

        val USER_C = UserProfile(
            id = "user_c_food_processing",
            name = "Mohan Lal",
            phoneNumber = "+91 97530 45678",
            email = "mohan.pickles@gmail.com",
            gender = "Male",
            age = 45,
            state = "Madhya Pradesh",
            district = "Ujjain",
            block = "Ghatiya",
            village = "Kalyanpura",
            pincode = "456006",
            areaType = "Rural",
            category = BusinessCategory.FOOD_PROCESSING,
            scale = BusinessScale.MICRO,
            productionCapacity = "500 Pickle Bottles/Mo",
            marginCapital = 75000.0,
            monthlyRevenue = 42000.0,
            monthlyExpenses = 21000.0,
            growthObjective = GrowthObjective.MODERNIZATION,
            businessDetails = "Traditional home-scale mango, lemon, and chili pickle production sold in village haats, seeking FSSAI compliance and semi-automated sealers.",
            promoterCategory = "SC",
            educationQualification = "8th Pass",
            hasEdpTraining = false
        )

        val EMPTY = UserProfile(id = "empty_user")
        val PRESETS = listOf(USER_A, USER_B, USER_C)
    }
}
