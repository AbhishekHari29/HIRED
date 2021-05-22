package com.droidevils.hired.Helper.Bean;

public class InsertIntoDatabase {

    public static void main() {

        Category[] categories = new Category[10];
        Service[] services = new Service[82];
//        AvailableService[] availableServices = new AvailableService[55];

        categories[0] = new Category("101", "HealthCare", "");
        categories[1] = new Category("102", "Home Service", "");
        categories[2] = new Category("103", "Personal Care", "");
        categories[3] = new Category("104", "Interior", "");
        categories[4] = new Category("105", "Software", "");
        categories[5] = new Category("106", "Business management and Marketing", "");
        categories[6] = new Category("107", "Craft, Arts and Design", "");
        categories[7] = new Category("108", "Construction and Building", "");
        categories[8] = new Category("109", "Education", "");
        categories[9] = new Category("110", "Finance", "");

        for (Category category : categories) {
            try {
                category.addCategory();
                System.out.println("Category added:"+category.getCategoryId());
            } catch (Exception throwable) {
                System.out.println(throwable.getLocalizedMessage());
                throwable.printStackTrace();
            }
        }

        // Fill Data
        services[0] = new Service("106001", "Purchasing Manager", "", "106", 1);
        services[1] = new Service("106002", "Sales Person", "", "106", 0);
        services[2] = new Service("106003", "Market Analyst", "", "106", 0);
        services[3] = new Service("106004", "Management consultant", "", "106", 1);
        services[4] = new Service("106005", "Receptionist", "", "106", 0);
        services[5] = new Service("105006", "App developer", "", "105", 0);
        services[6] = new Service("105007", "Software Engineer", "", "105", 1);
        services[7] = new Service("105008", "Computer Service and Repair", "", "105", 1);
        services[8] = new Service("105009", "System Analyst", "", "105", 1);
        services[9] = new Service("106010", "Personal Assistant", "", "106", 0);
        services[10] = new Service("107001", "Animator", "", "107", 0);
        services[11] = new Service("107002", "Fine artist", "", "107", 1);
        services[12] = new Service("107003", "Fashion designer", "", "107", 0);
        services[13] = new Service("107004", "Textile designer", "", "107", 0);
        services[14] = new Service("107005", "Product Designer", "", "107", 1);
        services[15] = new Service("107006", "Model maker", "", "107", 1);
        services[16] = new Service("107007", "Jewelery design and sales", "", "107", 0);
        services[17] = new Service("107008", "Tattooist", "", "107", 0);
        services[18] = new Service("107009", "Watch Repair", "", "107", 0);
        services[19] = new Service("107010", "Photographic Stylist", "", "107", 1);
        services[20] = new Service("108001", "Building Technician", "", "108", 0);
        services[21] = new Service("108002", "Carpenter", "", "108", 3);
        services[22] = new Service("108003", "Plumber", "", "108", 4);
        services[23] = new Service("108004", "Construction plant mechanics", "", "108", 0);
        services[24] = new Service("108005", "Civil Engineer", "", "108", 5);
        services[25] = new Service("108006", "Water treatment worker", "", "108", 0);
        services[26] = new Service("108007", "Leakage Operative", "", "108", 0);
        services[27] = new Service("108008", "Painter", "", "108", 0);
        services[28] = new Service("108009", "Landscape Architect", "", "108", 0);
        services[29] = new Service("108010", "Wall and floor tiler", "", "108", 0);
        services[30] = new Service("109001", "Tuition (10th,11th,12th class)", "", "109", 0);
        services[31] = new Service("109002", "IEEE/JEE Coaching", "", "109", 0);
        services[32] = new Service("109003", "Sports Coaching", "", "109", 0);
        services[33] = new Service("109004", "Other Languages Training", "", "109", 0);
        services[34] = new Service("109005", "NEET Coaching", "", "109", 0);
        services[35] = new Service("109006", "Spoken English class", "", "109", 0);
        services[36] = new Service("109007", "Career Adviser", "", "109", 0);
        services[37] = new Service("109008", "Teacher-Secondary School-All subjects", "", "109", 0);
        services[38] = new Service("109009", "counselling", "", "109", 0);
        services[39] = new Service("109010", "Home tuition", "", "109", 0);
        services[40] = new Service("110001", "Financial Adviser", "", "110", 0);
        services[41] = new Service("110002", "Accounting Technician", "", "110", 0);
        services[42] = new Service("110003", "Accountant", "", "110", 0);
        services[43] = new Service("110004", "Investment Analyst", "", "110", 0);
        services[44] = new Service("110005", "Accounts assistant", "", "110", 0);
        services[45] = new Service("110006", "Insurance Account manager", "", "110", 0);
        services[46] = new Service("104007", "Lighting Design", "", "104", 0);
        services[47] = new Service("102009", "Gardener", "", "102", 0);
        services[48] = new Service("104003", "Set Design", "", "104", 0);
        services[49] = new Service("104004", "Department Store Design", "", "104", 0);
        services[50] = new Service("101006", "Gym training", "", "101", 0);
        services[51] = new Service("101007", "Yoga Training", "", "101", 0);
        services[52] = new Service("101008", "Nutritionist", "", "101", 0);
        services[53] = new Service("101009", "Care taker", "", "101", 0);
        services[54] = new Service("101010", "Ambulance", "", "101", 0);
        services[55] = new Service("102002", "Cleaner", "", "102", 0);
        services[56] = new Service("102004", "Mechanic", "", "102", 0);
        services[57] = new Service("102006", "Car service", "", "102", 0);
        services[58] = new Service("102007", "Home products delivery", "", "102", 0);
        services[59] = new Service("102008", "Dry cleaner", "", "102", 0);
        services[60] = new Service("103001", "Beautician", "", "103", 0);
        services[61] = new Service("103002", "Nail Artist", "", "103", 0);
        services[62] = new Service("103003", "Make-up artist", "", "103", 0);
        services[63] = new Service("103004", "Beauty consultant", "", "103", 0);
        services[64] = new Service("103005", "Hairdresser", "", "103", 0);
        services[65] = new Service("103006", "Tailoring", "", "104", 0);
        services[66] = new Service("103007", "Body piercer", "", "103", 0);
        services[67] = new Service("103008", "Waxing", "", "103", 0);
        services[68] = new Service("104001", "Kitchen Interior", "", "104", 0);
        services[69] = new Service("104002", "Home interior", "", "104", 0);
        services[70] = new Service("105001", "IT project analyst", "", "105", 0);
        services[71] = new Service("105002", "IT trainer", "", "105", 0);
        services[72] = new Service("105003", "Game Designer", "", "105", 0);
        services[73] = new Service("105004", "Web editor", "", "105", 0);
        services[74] = new Service("105005", "Helpdesk professional", "", "105", 0);
        services[75] = new Service("101001", "Doctor", "", "101", 0);
        services[76] = new Service("101002", "Physician", "", "101", 0);
        services[77] = new Service("101003", "Nursing", "", "101", 0);
        services[78] = new Service("101004", "Physio therapist", "", "101", 0);
        services[79] = new Service("102001", "Electrician", "", "102", 0);
        services[80] = new Service("101005", "Therapist", "", "101", 0);
        services[81] = new Service("102005", "Dietian", "", "102", 0);

        for (Service service : services) {
            try {
                service.addService();
                System.out.println("Service Added:"+service.getServiceId());
            } catch (Exception throwable) {
                System.out.println(throwable.getLocalizedMessage());
                throwable.printStackTrace();
            }
        }


//        availableServices[0] = new AvailableService("akshaya@gmail.com", "101008", true, "8:00:00",
//                "14:00:00", "1111110", 4.5f);
//        availableServices[1] = new AvailableService("akshaya@gmail.com", "102004", true, "10:00:00",
//                "18:00:00", "0111110", 4.0f);
//        availableServices[2] = new AvailableService("akshaya@gmail.com", "103001", true, "10:00:00",
//                "18:00:00", "0111111", 4.0f);
//        availableServices[3] = new AvailableService("akshaya@gmail.com", "103005", true, "6:00:00",
//                "12:00:00", "0110110", 3.5f);
//        availableServices[4] = new AvailableService("akshaya@gmail.com", "104001", true, "16:00:00",
//                "20:30:00", "0111010", 3.5f);
//        availableServices[5] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "101008", true,
//                "8:00:00", "14:00:00", "1111110", 4.5f);
//        availableServices[6] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "102004", true,
//                "10:00:00", "18:00:00", "1111110", 4.0f);
//        availableServices[7] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "103001", true,
//                "6:00:00", "12:00:00", "0110110", 3.5f);
//        availableServices[8] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "103005", true,
//                "16:00:00", "20:30:00", "0111110", 4.5f);
//        availableServices[9] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "104001", true,
//                "8:00:00", "14:00:00", "0011110", 4.5f);
//        availableServices[10] = new AvailableService("subhashri3113@gmail.com", "101010", true, "8:00:00",
//                "14:00:00", "0111110", 4.0f);
//        availableServices[11] = new AvailableService("subhashri3113@gmail.com", "102006", true,
//                "10:00:00", "18:00:00", "0111110", 4.0f);
//        availableServices[12] = new AvailableService("subhashri3113@gmail.com", "103005", true, "6:00:00",
//                "12:00:00", "0001110", 4.0f);
//        availableServices[13] = new AvailableService("subhashri3113@gmail.com", "103006", true, "7:00:00",
//                "16:30:00", "0101110", 3.0f);
//        availableServices[14] = new AvailableService("subhashri3113@gmail.com", "104001", true, "8:00:00",
//                "14:00:00", "0111110", 4.0f);
//        availableServices[15] = new AvailableService("abhishekhari@gmail.com", "101008", true, "8:00:00",
//                "14:00:00", "1111110", 4.5f);
//        availableServices[16] = new AvailableService("abhishekhari@gmail.com", "102006", true, "8:00:00",
//                "8:00:00", "0111111", 4.5f);
//        availableServices[17] = new AvailableService("abhishekhari@gmail.com", "103001", true, "7:00:00",
//                "16:30:00", "1111111", 4.0f);
//        availableServices[18] = new AvailableService("abhishekhari@gmail.com", "103006", true, "8:00:00",
//                "15:00:00", "1110111", 3.0f);
//        availableServices[19] = new AvailableService("abhishekhari@gmail.com", "104001", true, "16:00:00",
//                "20:30:00", "0111110", 3.0f);
//        availableServices[20] = new AvailableService("kailash@gmail.com", "102007", true, "8:00:00",
//                "14:00:00", "0110110", 2.5f);
//        availableServices[21] = new AvailableService("kailash@gmail.com", "102008", true, "10:00:00",
//                "18:00:00", "1101110", 4.5f);
//        availableServices[22] = new AvailableService("kailash@gmail.com", "103002", true, "8:00:00",
//                "15:00:00", "0111110", 3.5f);
//        availableServices[23] = new AvailableService("kailash@gmail.com", "103008", true, "8:00:00",
//                "15:00:00", "0111111", 3.5f);
//        availableServices[24] = new AvailableService("kailash@gmail.com", "105006", true, "8:00:00",
//                "13:30:00", "0011110", 4.0f);
//        availableServices[25] = new AvailableService("akshayasaran25@gmail.com", "101009", true,
//                "8:00:00", "14:00:00", "0111111", 4.5f);
//        availableServices[26] = new AvailableService("akshayasaran25@gmail.com", "102008", true,
//                "10:00:00", "18:00:00", "1111110", 4.5f);
//        availableServices[27] = new AvailableService("akshayasaran25@gmail.com", "103003", true,
//                "7:00:00", "16:30:00", "0001110", 3.5f);
//        availableServices[28] = new AvailableService("akshayasaran25@gmail.com", "103008", true,
//                "8:00:00", "13:30:00", "0111110", 4.0f);
//        availableServices[29] = new AvailableService("akshayasaran25@gmail.com", "105006", true,
//                "16:00:00", "20:30:00", "0111110", 4.0f);
//        availableServices[30] = new AvailableService("subhashri.s2018@vitstudent.ac.in", "102008", true,
//                "8:00:00", "14:30:00", "1111111", 4.5f);
//        availableServices[31] = new AvailableService("subhashri.s2018@vitstudent.ac.in", "102008", true,
//                "16:00:00", "20:30:00", "1111111", 5.0f);
//        availableServices[32] = new AvailableService("subhashri.s2018@vitstudent.ac.in", "103002", true,
//                "8:00:00", "14:30:00", "1111110", 5.0f);
//        availableServices[33] = new AvailableService("subhashri.s2018@vitstudent.ac.in", "103008", true,
//                "7:00:00", "16:30:00", "1110110", 3.5f);
//        availableServices[34] = new AvailableService("subhashri.s2018@vitstudent.ac.in", "105006", true,
//                "8:00:00", "8:00:00", "0101110", 3.5f);
//        availableServices[35] = new AvailableService("abhishekhari12345@gmail.com", "101009", true,
//                "7:00:00", "16:30:00", "0111110", 4.0f);
//        availableServices[36] = new AvailableService("abhishekhari12345@gmail.com", "102004", true,
//                "10:00:00", "18:00:00", "1111111", 4.0f);
//        availableServices[37] = new AvailableService("abhishekhari12345@gmail.com", "102008", true,
//                "10:00:00", "18:00:00", "0101110", 3.0f);
//        availableServices[38] = new AvailableService("abhishekhari12345@gmail.com", "103001", true,
//                "8:00:00", "13:30:00", "0111110", 5.0f);
//        availableServices[39] = new AvailableService("abhishek.t2018@vitstudent.ac.in", "101010", true,
//                "7:00:00", "16:30:00", "1111110", 0.0f);
//        availableServices[40] = new AvailableService("abhishek.t2018@vitstudent.ac.in", "102004", true,
//                "10:00:00", "18:00:00", "1111111", 1.5f);
//        availableServices[41] = new AvailableService("abhishek.t2018@vitstudent.ac.in", "102008", true,
//                "10:00:00", "18:00:00", "1111110", 2.0f);
//        availableServices[42] = new AvailableService("abhishek.t2018@vitstudent.ac.in", "103002", true,
//                "8:00:00", "15:00:00", "0110011", 4.5f);
//        availableServices[43] = new AvailableService("abhishek.t2018@vitstudent.ac.in", "105008", true,
//                "8:00:00", "15:00:00", "0111110", 4.0f);
//        availableServices[44] = new AvailableService("akshayavarshini.s2018@vitstudent.ac.in", "105008", true,
//                "7:00:00", "16:30:00", "0111110", 4.0f);
//        availableServices[45] = new AvailableService("abhishekhari12345@gmail.com", "105008", true,
//                "10:00:00", "18:00:00", "0110100", 2.0f);
//        availableServices[46] = new AvailableService("akshayasaran25@gmail.com", "106002", true,
//                "8:00:00", "16:00:00", "1111111", 2.5f);
//        availableServices[47] = new AvailableService("kailash@gmail.com", "106002", true, "7:00:00",
//                "16:30:00", "0111110", 1.5f);
//        availableServices[48] = new AvailableService("subhashri3113@gmail.com", "108003", true, "8:00:00",
//                "13:30:00", "0110110", 3.0f);
//        availableServices[49] = new AvailableService("abhishekhari12345@gmail.com", "107001", true,
//                "8:00:00", "13:30:00", "1111111", 4.5f);
//        availableServices[50] = new AvailableService("akshayasaran25@gmail.com", "108003", true,
//                "6:00:00", "12:00:00", "0111110", 1.0f);
//        availableServices[51] = new AvailableService("kailash@gmail.com", "108003", true, "8:00:00",
//                "14:00:00", "0111110", 2.5f);
//        availableServices[52] = new AvailableService("subhashri3113@gmail.com", "109001", true, "8:00:00",
//                "15:00:00", "0111110", 4.0f);
//        availableServices[53] = new AvailableService("harshitha@gmail.com", "109001", true, "10:00:00",
//                "18:00:00", "0111110", 5.0f);
//        availableServices[54] = new AvailableService("harshitha@gmail.com", "109001", true, "7:00:00",
//                "16:30:00", "0111110", 4.4f);
//
//        for (AvailableService service : availableServices) {
//            try {
//                service.addService();
//                System.out.println("Service Added:" + service.getUserId() + ":" + service.getServiceId());
//            } catch (Exception throwable) {
//                System.out.println(throwable.getLocalizedMessage());
//                throwable.printStackTrace();
//            }
//        }

    }
}
