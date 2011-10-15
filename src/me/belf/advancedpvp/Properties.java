package me.belf.advancedpvp;

import java.io.File;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;


public class Properties 
{
    public String path = "plugins/AdvancedPvP/";

    public static Boolean pvp_only, cooldown_on_damages_only, cooldown_on_change;        
    public static HashMap<Integer, Integer> confCooldowns = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Integer> confDamages = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Double> confBackstab = new HashMap<Integer, Double>();
    public static HashMap<Integer, Double> confParryProbability = new HashMap<Integer, Double>(); 
    public static HashMap<Integer, Double> confParryKnockbackPower = new HashMap<Integer, Double>();

    public static HashMap<Integer, Double> confBlockingProbability = new HashMap<Integer, Double>();    
    public static HashMap<Integer, Double> confBlockingKnockbackPower = new HashMap<Integer, Double>();          
    public static HashMap<Integer, Integer> confBlockingDamages = new HashMap<Integer, Integer>();
    public static HashMap<Integer, Double> confCounterattack = new HashMap<Integer, Double>();
    public static HashMap<Integer, Double> confKnockbackPower = new HashMap<Integer, Double>();
    public static HashMap<Integer, Double> confKnockbackProbability = new HashMap<Integer, Double>();       
    public static HashMap<Integer, Double> confDisarm = new HashMap<Integer, Double>();

    public static HashMap<Integer, Double> confCriticalProbability = new HashMap<Integer, Double>();
    public static HashMap<Integer, Integer> confCriticalExtraDamages = new HashMap<Integer, Integer>();


    private int key_toInt;
    private PluginDescriptionFile description;
    private String prefix;

    File file = new File(path + File.separator + "config.yml");
    static Configuration config = null;

    public void checkProperties()
    {

        new File(path).mkdir();
        config = load();
        if(!file.exists())
        {
            try 
            {
                    file.createNewFile();
                    createProperties();
            } 
            catch (Exception ex) 
            {
                    ex.printStackTrace();
            }
        } 
        else 
        {
                loadProperties();
        }
    }


        private Configuration load()
        {
            try {
                Configuration configx = new Configuration(file);
                configx.load();
                return configx;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void createProperties()
        {
            System.out.println(AdvancedPvP.prefix + "No file found - Writing default values...");  	

            //Put in defaults
            config.setProperty("General.pvp-only", false);              
            config.setProperty("General.cooldown-on-damages-only", false);
            config.setProperty("General.cooldown_on_change", false);

            config.setProperty("Weapons.261.Cooldown", 5000);
            config.setProperty("Weapons.268.Cooldown", 1500);
            config.setProperty("Weapons.269.Cooldown", 1200);
            config.setProperty("Weapons.270.Cooldown", 1200);
            config.setProperty("Weapons.271.Cooldown", 2500);
            config.setProperty("Weapons.290.Cooldown", 1200);
            config.setProperty("Weapons.0.Cooldown", 1000);                

            config.setProperty("Weapons.261.Base-Damages", 4);
            config.setProperty("Weapons.268.Base-Damages", 3);
            config.setProperty("Weapons.269.Base-Damages", 2);
            config.setProperty("Weapons.270.Base-Damages", 2);
            config.setProperty("Weapons.271.Base-Damages", 5);
            config.setProperty("Weapons.290.Base-Damages", 3);
            config.setProperty("Weapons.0.Base-Damages", 2);                 

            config.setProperty("Weapons.261.Backstab", 1.5);
            config.setProperty("Weapons.268.Backstab", 1.5);
            config.setProperty("Weapons.269.Backstab", 1.5);
            config.setProperty("Weapons.270.Backstab", 1.5);
            config.setProperty("Weapons.271.Backstab", 1.5);
            config.setProperty("Weapons.290.Backstab", 0);
            config.setProperty("Weapons.0.Backstab", 0);                  

            config.setProperty("Weapons.261.Disarm", 0.05);
            config.setProperty("Weapons.268.Disarm", 0.05);
            config.setProperty("Weapons.269.Disarm", 0.05);
            config.setProperty("Weapons.270.Disarm", 0.05);
            config.setProperty("Weapons.271.Disarm", 0.05);
            config.setProperty("Weapons.290.Disarm", 0.05);
            config.setProperty("Weapons.0.Disarm", 0.05);                   

            config.setProperty("Weapons.261.Counterattack", 0);
            config.setProperty("Weapons.268.Counterattack", 0.05);
            config.setProperty("Weapons.269.Counterattack", 0.05);
            config.setProperty("Weapons.270.Counterattack", 0.05);
            config.setProperty("Weapons.271.Counterattack", 0.05);
            config.setProperty("Weapons.290.Counterattack", 0.05);
            config.setProperty("Weapons.0.Counterattack", 0.05);                 


            config.setProperty("Weapons.261.Parry.Probability", 0);
            config.setProperty("Weapons.268.Parry.Probability", 0.1);
            config.setProperty("Weapons.269.Parry.Probability", 0.05);
            config.setProperty("Weapons.270.Parry.Probability", 0.05);
            config.setProperty("Weapons.271.Parry.Probability", 0.1);
            config.setProperty("Weapons.290.Parry.Probability", 0.05);
            config.setProperty("Weapons.0.Parry.Probability", 0); 

            config.setProperty("Weapons.261.Parry.KnockbackPower", 0);
            config.setProperty("Weapons.268.Parry.KnockbackPower", 0.3);
            config.setProperty("Weapons.269.Parry.KnockbackPower", 0.3);
            config.setProperty("Weapons.270.Parry.KnockbackPower", 0.3);
            config.setProperty("Weapons.271.Parry.KnockbackPower", 0.3);
            config.setProperty("Weapons.290.Parry.KnockbackPower", 0.3);
            config.setProperty("Weapons.0.Parry.KnockbackPower", 0);                 


            config.setProperty("Weapons.261.Blocking.Probability", 0.1);
            config.setProperty("Weapons.268.Blocking.Probability", 0.1);
            config.setProperty("Weapons.269.Blocking.Probability", 0.05);
            config.setProperty("Weapons.270.Blocking.Probability", 0.05);
            config.setProperty("Weapons.271.Blocking.Probability", 0.1);
            config.setProperty("Weapons.290.Blocking.Probability", 0.05);
            config.setProperty("Weapons.0.Blocking.Probability", 0); 

            config.setProperty("Weapons.261.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.268.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.269.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.270.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.271.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.290.Blocking.KnockbackPower", 0.3);
            config.setProperty("Weapons.0.Blocking.KnockbackPower", 0); 

            config.setProperty("Weapons.261.Blocking.Damages", 5);
            config.setProperty("Weapons.268.Blocking.Damages", 5);
            config.setProperty("Weapons.269.Blocking.Damages", 4);
            config.setProperty("Weapons.270.Blocking.Damages", 4);
            config.setProperty("Weapons.271.Blocking.Damages", 4);
            config.setProperty("Weapons.290.Blocking.Damages", 4);
            config.setProperty("Weapons.0.Blocking.Damages", 0); 


            config.setProperty("Weapons.261.Knockback.Probability", 0.1);
            config.setProperty("Weapons.268.Knockback.Probability", 0.1);
            config.setProperty("Weapons.269.Knockback.Probability", 0.05);
            config.setProperty("Weapons.270.Knockback.Probability", 0.05);
            config.setProperty("Weapons.271.Knockback.Probability", 0.1);
            config.setProperty("Weapons.290.Knockback.Probability", 0.05);
            config.setProperty("Weapons.0.Knockback.Probability", 0); 

            config.setProperty("Weapons.261.Knockback.Power", 0.2);
            config.setProperty("Weapons.268.Knockback.Power", 0.2);
            config.setProperty("Weapons.269.Knockback.Power", 0.2);
            config.setProperty("Weapons.270.Knockback.Power", 0.2);
            config.setProperty("Weapons.271.Knockback.Power", 0.2);
            config.setProperty("Weapons.290.Knockback.Power", 0.2);
            config.setProperty("Weapons.0.Knockback.Power", 0); 


            config.setProperty("Weapons.261.Critical.Probability", 0.1);
            config.setProperty("Weapons.268.Critical.Probability", 0.1);
            config.setProperty("Weapons.269.Critical.Probability", 0.05);
            config.setProperty("Weapons.270.Critical.Probability", 0.05);
            config.setProperty("Weapons.271.Critical.Probability", 0.1);
            config.setProperty("Weapons.290.Critical.Probability", 0.05);
            config.setProperty("Weapons.0.Critical.Probability", 0);     

            config.setProperty("Weapons.261.Critical.Extra-damages", 2);
            config.setProperty("Weapons.268.Critical.Extra-damages", 2);
            config.setProperty("Weapons.269.Critical.Extra-damages", 2);
            config.setProperty("Weapons.270.Critical.Extra-damages", 2);
            config.setProperty("Weapons.271.Critical.Extra-damages", 2);
            config.setProperty("Weapons.290.Critical.Extra-damages", 2);
            config.setProperty("Weapons.0.Critical.Extra-damages", 2);

            config.save();

            loadProperties();
        }

        private void loadProperties()
        {             
            System.out.println(AdvancedPvP.prefix + "Loading config.yml...");


            pvp_only = config.getBoolean("General.pvp-only", false);
            cooldown_on_damages_only = config.getBoolean("General.cooldown-on-damages-only", false);
            cooldown_on_change = config.getBoolean("General.cooldown_on_change", false);


            // *** Weapons stats *** //        
            ConfigurationNode node = config.getNode("Weapons");                
            if(node!=null) {
                for(String key : node.getKeys()) {                

                    key_toInt = Integer.parseInt(key);
                    

                    confCooldowns.put(key_toInt, config.getInt("Weapons." + key + ".Cooldown", 0));
                    confDamages.put(key_toInt, config.getInt("Weapons." + key + ".Base-Damages", -1));
                    confBackstab.put(key_toInt, config.getDouble("Weapons." + key + ".Backstab", 0));
                    confDisarm.put(key_toInt, config.getDouble("Weapons." + key + ".Disarm", 0));
                    confCounterattack.put(key_toInt, config.getDouble("Weapons." + key + ".Counterattack", 0));
                    
                    confParryProbability.put(key_toInt, config.getDouble("Weapons." + key + ".Parry.Probability", 0));
                    confParryKnockbackPower.put(key_toInt, config.getDouble("Weapons." + key + ".Parry.KnockbackPower", 0));
                    
                    confBlockingProbability.put(key_toInt, config.getDouble("Weapons." + key + ".Blocking.Probability", 0));
                    confBlockingKnockbackPower.put(key_toInt, config.getDouble("Weapons." + key + ".Blocking.KnockbackPower", 0));
                    confBlockingDamages.put(key_toInt, config.getInt("Weapons." + key + ".Blocking.Damages", 0));
                    
                    confKnockbackProbability.put(key_toInt, config.getDouble("Weapons." + key + ".Knockback.Probability", 0));
                    confKnockbackPower.put(key_toInt, config.getDouble("Weapons." + key + ".Knockback.Power", 0));
                    
                    confCriticalProbability.put(key_toInt, config.getDouble("Weapons." + key + ".Critical.Probability", 0));
                    confCriticalExtraDamages.put(key_toInt, config.getInt("Weapons." + key + ".Critical.Extra-damages", 0));
                    
                }
            }        
        }

        public HashMap<Integer, Integer> getConfCooldowns() {                
            return confCooldowns;
        }

    }