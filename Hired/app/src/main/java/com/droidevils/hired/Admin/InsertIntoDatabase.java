package com.droidevils.hired.Admin;

import android.util.Log;

import com.droidevils.hired.Helper.Bean.AvailableService;
import com.droidevils.hired.Helper.Bean.AvailableServiceInterface;

public class InsertIntoDatabase {

    public static void main() {

//        Category[] categories = new Category[10];
//        Service[] services = new Service[82];
        AvailableService[] availableServices = new AvailableService[55];

//        categories[0] = new Category("101", "HealthCare", "");
//        categories[1] = new Category("102", "Home Service", "");
//        categories[2] = new Category("103", "Personal Care", "");
//        categories[3] = new Category("104", "Interior", "");
//        categories[4] = new Category("105", "Software", "");
//        categories[5] = new Category("106", "Business management and Marketing", "");
//        categories[6] = new Category("107", "Craft, Arts and Design", "");
//        categories[7] = new Category("108", "Construction and Building", "");
//        categories[8] = new Category("109", "Education", "");
//        categories[9] = new Category("110", "Finance", "");
//
//        for (Category category : categories) {
//            try {
//                category.addCategory();
//                System.out.println("Category added:"+category.getCategoryId());
//            } catch (Exception throwable) {
//                System.out.println(throwable.getLocalizedMessage());
//                throwable.printStackTrace();
//            }
//        }

        // Fill Data
//        services[0] = new Service("106001", "Purchasing Manager", "", "106", 1);
//        services[1] = new Service("106002", "Sales Person","Sales Person", "", "106", 0);
//        services[2] = new Service("106003", "Market Analyst", "", "106", 0);
//        services[3] = new Service("106004", "Management consultant", "", "106", 1);
//        services[4] = new Service("106005", "Receptionist", "", "106", 0);
//        services[5] = new Service("105006", "App developer", "App developer", "", "105", 0);
//        services[6] = new Service("105007", "Software Engineer", "", "105", 1);
//        services[7] = new Service("105008", "Computer Service and Repair", "Computer Service and Repair", "", "105", 1);
//        services[8] = new Service("105009", "System Analyst", "", "105", 1);
//        services[9] = new Service("106010", "Personal Assistant", "", "106", 0);
//        services[10] = new Service("107001", "Animator","Animator", "", "107", 0);
//        services[11] = new Service("107002", "Fine artist", "", "107", 1);
//        services[12] = new Service("107003", "Fashion designer", "", "107", 0);
//        services[13] = new Service("107004", "Textile designer", "", "107", 0);
//        services[14] = new Service("107005", "Product Designer", "", "107", 1);
//        services[15] = new Service("107006", "Model maker", "", "107", 1);
//        services[16] = new Service("107007", "Jewelery design and sales", "", "107", 0);
//        services[17] = new Service("107008", "Tattooist", "", "107", 0);
//        services[18] = new Service("107009", "Watch Repair", "", "107", 0);
//        services[19] = new Service("107010", "Photographic Stylist", "", "107", 1);
//        services[20] = new Service("108001", "Building Technician", "", "108", 0);
//        services[21] = new Service("108002", "Carpenter", "", "108", 3);
//        services[22] = new Service("108003", "Plumber","Plumber", "", "108", 4);
//        services[23] = new Service("108004", "Construction plant mechanics", "", "108", 0);
//        services[24] = new Service("108005", "Civil Engineer", "", "108", 5);
//        services[25] = new Service("108006", "Water treatment worker", "", "108", 0);
//        services[26] = new Service("108007", "Leakage Operative", "", "108", 0);
//        services[27] = new Service("108008", "Painter", "", "108", 0);
//        services[28] = new Service("108009", "Landscape Architect", "", "108", 0);
//        services[29] = new Service("108010", "Wall and floor tiler", "", "108", 0);
//        services[30] = new Service("109001", "Tuition" "Tuition (10th,11th,12th class)", "", "109", 0);
//        services[31] = new Service("109002", "IEEE/JEE Coaching", "", "109", 0);
//        services[32] = new Service("109003", "Sports Coaching", "", "109", 0);
//        services[33] = new Service("109004", "Other Languages Training", "", "109", 0);
//        services[34] = new Service("109005", "NEET Coaching", "", "109", 0);
//        services[35] = new Service("109006", "Spoken English class", "", "109", 0);
//        services[36] = new Service("109007", "Career Adviser", "", "109", 0);
//        services[37] = new Service("109008", "Teacher-Secondary School-All subjects", "", "109", 0);
//        services[38] = new Service("109009", "counselling", "", "109", 0);
//        services[39] = new Service("109010", "Home tuition", "", "109", 0);
//        services[40] = new Service("110001", "Financial Adviser", "", "110", 0);
//        services[41] = new Service("110002", "Accounting Technician", "", "110", 0);
//        services[42] = new Service("110003", "Accountant", "", "110", 0);
//        services[43] = new Service("110004", "Investment Analyst", "", "110", 0);
//        services[44] = new Service("110005", "Accounts assistant", "", "110", 0);
//        services[45] = new Service("110006", "Insurance Account manager", "", "110", 0);
//        services[46] = new Service("104007", "Lighting Design", "", "104", 0);
//        services[47] = new Service("102009", "Gardener", "", "102", 0);
//        services[48] = new Service("104003", "Set Design", "", "104", 0);
//        services[49] = new Service("104004", "Department Store Design", "", "104", 0);
//        services[50] = new Service("101006", "Gym training", "", "101", 0);
//        services[51] = new Service("101007", "Yoga Training", "", "101", 0);
//        services[52] = new Service("101008", "Nutritionist", "Nutritionist", "", "101", 0);
//        services[53] = new Service("101009", "Care taker","Care taker", "", "101", 0);
//        services[54] = new Service("101010", "Ambulance", "Ambulance", "", "101", 0);
//        services[55] = new Service("102002", "Cleaner", "", "102", 0);
//        services[56] = new Service("102004", "Mechanic",, "Mechanic", "", "102", 0);
//        services[57] = new Service("102006", "Car service", "Car service", "", "102", 0);
//        services[58] = new Service("102007", "Home products delivery", "Home products delivery", "", "102", 0);
//        services[59] = new Service("102008", "Dry cleaner","Dry cleaner", "", "102", 0);
//        services[60] = new Service("103001", "Beautician", "Beautician", "", "103", 0);
//        services[61] = new Service("103002", "Nail Artist", "Nail Artist", "", "103", 0);
//        services[62] = new Service("103003", "Make-up artist","Make-up artist", "", "103", 0);
//        services[63] = new Service("103004", "Beauty consultant", "", "103", 0);
//        services[64] = new Service("103005", "Hairdresser", "Hairdresser", "", "103", 0);
//        services[65] = new Service("103006", "Tailoring","Tailoring", "", "104", 0);
//        services[66] = new Service("103007", "Body piercer", "", "103", 0);
//        services[67] = new Service("103008", "Waxing", "Waxing", "", "103", 0);
//        services[68] = new Service("104001", "Kitchen Interior", "Kitchen Interior", "", "104", 0);
//        services[69] = new Service("104002", "Home interior", "", "104", 0);
//        services[70] = new Service("105001", "IT project analyst", "", "105", 0);
//        services[71] = new Service("105002", "IT trainer", "", "105", 0);
//        services[72] = new Service("105003", "Game Designer", "", "105", 0);
//        services[73] = new Service("105004", "Web editor", "", "105", 0);
//        services[74] = new Service("105005", "Helpdesk professional", "", "105", 0);
//        services[75] = new Service("101001", "Doctor", "", "101", 0);
//        services[76] = new Service("101002", "Physician", "", "101", 0);
//        services[77] = new Service("101003", "Nursing", "", "101", 0);
//        services[78] = new Service("101004", "Physio therapist", "", "101", 0);
//        services[79] = new Service("102001", "Electrician", "", "102", 0);
//        services[80] = new Service("101005", "Therapist", "", "101", 0);
//        services[81] = new Service("102005", "Dietian", "", "102", 0);

//        for (Service service : services) {
//            try {
//                service.addService();
//                System.out.println("Service Added:"+service.getServiceId());
//            } catch (Exception throwable) {
//                System.out.println(throwable.getLocalizedMessage());
//                throwable.printStackTrace();
//            }
//        }


        availableServices[0] = new AvailableService("2gC80zDErVPeajeIZ2Km04qwObw1", "Akshayavarshini", "101008", "Nutritionist", true, "8:00 AM",
                "02:00 PM", "1111110", 4.5f);
        availableServices[1] = new AvailableService("2gC80zDErVPeajeIZ2Km04qwObw1", "Akshayavarshini", "102004", "Mechanic", true, "10:00 AM",
                "08:00 AM", "0111110", 4.0f);
        availableServices[2] = new AvailableService("2gC80zDErVPeajeIZ2Km04qwObw1", "Akshayavarshini", "103001", "Beautician", true, "10:00 AM",
                "08:00 AM", "0111111", 4.0f);
        availableServices[3] = new AvailableService("2gC80zDErVPeajeIZ2Km04qwObw1", "Akshayavarshini", "103005", "Hairdresser", true, "06:00 AM",
                "12:00 PM", "0110110", 3.5f);
        availableServices[4] = new AvailableService("2gC80zDErVPeajeIZ2Km04qwObw1", "Akshayavarshini", "104001", "Kitchen Interior", true, "04:00 PM",
                "08:30 PM", "0111010", 3.5f);
        availableServices[5] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "101008", "Nutritionist", true,
                "8:00 AM", "02:00 PM", "1111110", 4.5f);
        availableServices[6] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "102004", "Mechanic", true,
                "10:00 AM", "08:00 AM", "1111110", 4.0f);
        availableServices[7] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "103001", "Beautician", true,
                "06:00 AM", "12:00 PM", "0110110", 3.5f);
        availableServices[8] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "103005", "Hairdresser", true,
                "04:00 PM", "08:30 PM", "0111110", 4.5f);
        availableServices[9] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "104001", "Kitchen Interior", true,
                "8:00 AM", "02:00 PM", "0011110", 4.5f);
        availableServices[10] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "101010", "Ambulance", true, "8:00 AM",
                "02:00 PM", "0111110", 4.0f);
        availableServices[11] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "102006", "Car service", true,
                "10:00 AM", "08:00 AM", "0111110", 4.0f);
        availableServices[12] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "103005", "Hairdresser", true, "06:00 AM",
                "12:00 PM", "0001110", 4.0f);
        availableServices[13] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "103006", "Tailoring",true, "07:00 AM",
                "04:30 PM", "0101110", 3.0f);
        availableServices[14] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "104001", "Kitchen Interior", true, "8:00 AM",
                "02:00 PM", "0111110", 4.0f);
        availableServices[15] = new AvailableService("Ep7uJofh8yXi10d98HJiGE5m7J32", "Abhishek", "101008", "Nutritionist", true, "8:00 AM",
                "02:00 PM", "1111110", 4.5f);
        availableServices[16] = new AvailableService("Ep7uJofh8yXi10d98HJiGE5m7J32", "Abhishek", "102006", "Car service", true, "8:00 AM",
                "8:00 AM", "0111111", 4.5f);
        availableServices[17] = new AvailableService("Ep7uJofh8yXi10d98HJiGE5m7J32", "Abhishek", "103001", "Beautician", true, "07:00 AM",
                "04:30 PM", "1111111", 4.0f);
        availableServices[18] = new AvailableService("Ep7uJofh8yXi10d98HJiGE5m7J32", "Abhishek", "103006", "Tailoring",true, "8:00 AM",
                "03:00 PM", "1110111", 3.0f);
        availableServices[19] = new AvailableService("Ep7uJofh8yXi10d98HJiGE5m7J32", "Abhishek", "104001", "Kitchen Interior", true, "04:00 PM",
                "08:30 PM", "0111110", 3.0f);
        availableServices[20] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "102007", "Home products delivery", true, "8:00 AM",
                "02:00 PM", "0110110", 2.5f);
        availableServices[21] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "102008", "Dry cleaner",true, "10:00 AM",
                "08:00 AM", "1101110", 4.5f);
        availableServices[22] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "103002", "Nail Artist", true, "8:00 AM",
                "03:00 PM", "0111110", 3.5f);
        availableServices[23] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "103008", "Waxing", true, "8:00 AM",
                "03:00 PM", "0111111", 3.5f);
        availableServices[24] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "105006", "App developer", true, "8:00 AM",
                "01:30 PM", "0011110", 4.0f);
        availableServices[25] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "101009", "Care taker",true,
                "8:00 AM", "02:00 PM", "0111111", 4.5f);
        availableServices[26] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "102008", "Dry cleaner",true,
                "10:00 AM", "08:00 AM", "1111110", 4.5f);
        availableServices[27] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "103003", "Make-up artist",true,
                "07:00 AM", "04:30 PM", "0001110", 3.5f);
        availableServices[28] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "103008", "Waxing", true,
                "8:00 AM", "01:30 PM", "0111110", 4.0f);
        availableServices[29] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "105006", "App developer", true,
                "04:00 PM", "08:30 PM", "0111110", 4.0f);
        availableServices[30] = new AvailableService("nVi1F48HUwSUNKpCmrY9hqeUK1U2", "Subha Shri S", "102008", "Dry cleaner",true,
                "8:00 AM", "02:30 PM", "1111111", 4.5f);
        availableServices[31] = new AvailableService("nVi1F48HUwSUNKpCmrY9hqeUK1U2", "Subha Shri S", "102008", "Dry cleaner",true,
                "04:00 PM", "08:30 PM", "1111111", 5.0f);
        availableServices[32] = new AvailableService("nVi1F48HUwSUNKpCmrY9hqeUK1U2", "Subha Shri S", "103002", "Nail Artist", true,
                "8:00 AM", "02:30 PM", "1111110", 5.0f);
        availableServices[33] = new AvailableService("nVi1F48HUwSUNKpCmrY9hqeUK1U2", "Subha Shri S", "103008", "Waxing", true,
                "07:00 AM", "04:30 PM", "1110110", 3.5f);
        availableServices[34] = new AvailableService("nVi1F48HUwSUNKpCmrY9hqeUK1U2", "Subha Shri S", "105006", "App developer", true,
                "8:00 AM", "8:00 AM", "0101110", 3.5f);
        availableServices[35] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "101009", "Care taker",true,
                "07:00 AM", "04:30 PM", "0111110", 4.0f);
        availableServices[36] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "102004", "Mechanic", true,
                "10:00 AM", "08:00 AM", "1111111", 4.0f);
        availableServices[37] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "102008", "Dry cleaner",true,
                "10:00 AM", "08:00 AM", "0101110", 3.0f);
        availableServices[38] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "103001", "Beautician", true,
                "8:00 AM", "01:30 PM", "0111110", 5.0f);
        availableServices[39] = new AvailableService("BaHRNs02e7X6P8txt3ZF8reVB6y1", "Abhishek Hari", "101010", "Ambulance", true,
                "07:00 AM", "04:30 PM", "1111110", 0.0f);
        availableServices[40] = new AvailableService("BaHRNs02e7X6P8txt3ZF8reVB6y1", "Abhishek Hari", "102004", "Mechanic", true,
                "10:00 AM", "08:00 AM", "1111111", 1.5f);
        availableServices[41] = new AvailableService("BaHRNs02e7X6P8txt3ZF8reVB6y1", "Abhishek Hari", "102008", "Dry cleaner",true,
                "10:00 AM", "08:00 AM", "1111110", 2.0f);
        availableServices[42] = new AvailableService("BaHRNs02e7X6P8txt3ZF8reVB6y1", "Abhishek Hari", "103002", "Nail Artist", true,
                "8:00 AM", "03:00 PM", "0110011", 4.5f);
        availableServices[43] = new AvailableService("BaHRNs02e7X6P8txt3ZF8reVB6y1", "Abhishek Hari", "105008", "Computer Service and Repair", true,
                "8:00 AM", "03:00 PM", "0111110", 4.0f);
        availableServices[44] = new AvailableService("bFfh6XHbgzVO47q2o2BIaa0ymT72", "Akshaya Saran", "105008", "Computer Service and Repair", true,
                "07:00 AM", "04:30 PM", "0111110", 4.0f);
        availableServices[45] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "105008", "Computer Service and Repair", true,
                "10:00 AM", "08:00 AM", "0110100", 2.0f);
        availableServices[46] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "106002", "Sales Person",true,
                "8:00 AM", "04:00 PM", "1111111", 2.5f);
        availableServices[47] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "106002", "Sales Person",true, "07:00 AM",
                "04:30 PM", "0111110", 1.5f);
        availableServices[48] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "108003", "Plumber",true, "8:00 AM",
                "01:30 PM", "0110110", 3.0f);
        availableServices[49] = new AvailableService("wI59zdKVx7fiKLj33szg1bYEbVJ2", "Abhishek", "107001", "Animator",true,
                "8:00 AM", "01:30 PM", "1111111", 4.5f);
        availableServices[50] = new AvailableService("uyxopLrFf9VoXhQ6EQf23vFlbiA3", "Akshaya", "108003", "Plumber",true,
                "06:00 AM", "12:00 PM", "0111110", 1.0f);
        availableServices[51] = new AvailableService("W7PUAGsDr3bCi7yUxg69lXPQcNm2", "Kailash S", "108003", "Plumber",true, "8:00 AM",
                "02:00 PM", "0111110", 2.5f);
        availableServices[52] = new AvailableService("jp8oKv2uWGRDttThEzoKcImbvLu2", "Subha Shri", "109001", "Tuition", true, "8:00 AM",
                "03:00 PM", "0111110", 4.0f);
        availableServices[53] = new AvailableService("tMdk97vRjfffizLzoKlYXhW6yND2", "Harshitha", "109001", "Tuition", true, "10:00 AM",
                "08:00 AM", "0111110", 5.0f);
        availableServices[54] = new AvailableService("tMdk97vRjfffizLzoKlYXhW6yND2", "Harshitha", "109001", "Tuition" ,true, "07:00 AM",
                "04:30 PM", "0111110", 4.4f);

        for (AvailableService service : availableServices) {
            service.addService(new AvailableServiceInterface() {
                @Override
                public void getBooleanResult(Boolean result) {
                    Log.i("MESSAGE", "Service Added: "+result);
                }
            });
        }

    }
}
