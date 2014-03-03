package models;

import play.db.ebean.Model;

/**
 * This model is dedicated for verifying health card
 */
public class HealthCard extends Model {

    /**
     * This function for dedicated for verify for health card number.
     * It has all the algorithms for verifying the health card
     *
     * @param Long healthCardNumber
     * @return boolean true|false
     */
    public static boolean veryfyHealthCard(Long healthCardNumber) {
        return (healthCardNumber > 1000000 && healthCardNumber <99999999);
    }
}
