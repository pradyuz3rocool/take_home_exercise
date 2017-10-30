package com.shutl.controller;

import com.shutl.enums.Vehicle;
import com.shutl.model.Quote;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class QuoteController {

    private static final long DTP_CONSTANT = 100000000;
    private static final String CARRIER_DATA = "src" + File.separator + "data" + File.separator + "carrier_data.json";

    @RequestMapping(value = "/quote", method = RequestMethod.POST)
    public @ResponseBody
    Quote quote(@RequestBody Quote quote) {

        Long price = (Math.abs((Long.valueOf(quote.getDeliveryPostcode(), 36) - Long.valueOf(quote.getPickupPostcode(), 36)))) / DTP_CONSTANT;

        if (quote.getVehicle() != null) {
            try {

                JSONArray priceList = this.getPriceListJSON(price, quote.getVehicle());
                return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), price, quote.getVehicle().toString(), priceList);

            } catch (FileNotFoundException e) {
                System.err.println("Carrier data (resource) file could not be found at \"" + CARRIER_DATA + "\"");

            } catch (ParseException e) {
                System.err.println("Encountered error while parsing JSON file at \"" + CARRIER_DATA + "\"");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), price);
    }

    private JSONArray getPriceListJSON(Long initialPrice, Vehicle vehicle) throws ParseException, IOException {

        JSONArray responseArray = new JSONArray();

        JSONParser parser = new JSONParser();
        Object fileData = parser.parse(new FileReader(CARRIER_DATA));
        JSONArray jsonArray = (JSONArray) fileData;

        for (Object object : jsonArray) {

            JSONObject carrier = (JSONObject) object;
            Object carrierServicesObject = carrier.get("services");
            JSONArray carrierServices = (JSONArray) carrierServicesObject;

            for (Object servicesObject : carrierServices) {

                JSONObject services = (JSONObject) servicesObject;

                Long serviceMarkupPrice = new Long(services.get("markup").toString());
                String deliveryTime = services.get("delivery_time").toString();
                JSONArray vehicles = (JSONArray) services.get("vehicles");

                if (vehicles.contains(vehicle.toString())) {
                    // Safe zone!
                    // Let's prepare the response
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("service", carrier.get("carrier_name").toString());
                    responseObj.put("price", this.calcPriceByService(initialPrice, new Long(carrier.get("base_price").toString()), serviceMarkupPrice, vehicle));
                    responseObj.put("delivery_time", deliveryTime);

                    responseArray.add(responseObj);
                }
            }
        }
        //System.out.println(responseArray);
        return responseArray;
    }

    /**
     * This is a helper method for getPriceListJSON method
     * Price calculations can be manipulated easily in the future by editing this method
     *
     * @param initialPrice       Price before any markups
     * @param carrierBasePrice   Base price asked by the carrier
     * @param serviceMarkupPrice Service specific markup price
     * @param vehicle            Vehicle type required for vehicle markup calculation
     * @return final price after markups
     */
    private Long calcPriceByService(Long initialPrice, Long carrierBasePrice, Long serviceMarkupPrice, Vehicle vehicle) {
        // Add vehicle markup and remaining markups
        return (vehicle.getMarkupAmount(initialPrice) + carrierBasePrice + serviceMarkupPrice);
    }

    /**
     * Not the best place (wrong controller) or practice to display HTML
     * But had to do a bit of improvisation here to keep it simple
     *
     * @return HTML content
     * @throws IOException
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public @ResponseBody
    String index() throws IOException {

        FileReader fr = new FileReader("src" + File.separator + "static" + File.separator + "html" + File.separator + "index.html");
        BufferedReader br = new BufferedReader(fr);
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            // Better to put string concat separately than as parameter
            line = line + System.lineSeparator();
            content.append(line);
        }
        return content.toString();
    }

}
