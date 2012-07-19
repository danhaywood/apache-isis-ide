package com.halware.eclipseutil.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * Static functions for generating randomness.
 * All methods are thread-safe for concurrent use.
 */
public class RandomUtil {
    
    /**
     * Name for auto-generation of subtypes.
     */
    public static final String SUBTYPE_SUFFIX = "_child_" ; //$NON-NLS-1$
    
    /**
     * Number of subtypes.
     */
    public static final int MIN_SUBTYPES = 3; 
    
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in last year.
     */
    public static final int DATE_HINT_LAST_YEAR = 1;
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in last month.
     */
    public static final int DATE_HINT_LAST_MONTH = 2;
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in last week.
     */
    public static final int DATE_HINT_LAST_WEEK = 3;
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in next year.
     */
    public static final int DATE_HINT_NEXT_YEAR = 4;
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in next month.
     */
    public static final int DATE_HINT_NEXT_MONTH = 5;
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * in next week.
     */
    public static final int DATE_HINT_NEXT_WEEK = 6; 
    /**
     * Hint for <code>getDate(int)</code> to return random date
     * ibetween 20 and 65 years in the past.
     */
    public static final int DATE_HINT_DOB = 7;
    
    
    private static final String PREFERRED_DIGEST_ALGORITHM = "MD5";  //$NON-NLS-1$
    private static final char[] CHARACTERS;
    
    static {
        CHARACTERS = new char[52];        
        for (int i = 0; i < 26; i ++) {            
            RandomUtil.CHARACTERS[i] = (char) (97 + i);            
            RandomUtil.CHARACTERS[i + 26] = (char) (65 + i);        
        }        
    }
    
    //  no direct access to these fields - use getters
    private static MessageDigest _digest = null; 
    private static Random _random = null; 
    private static SecureRandom _secureRandom = null; 
    private static String _hostName = null ;  
    
    /**
     * Return a random universal identifier.  Not particularly secure 
     * as is relatively predictable.
     * Based on class RandomGUID by Marc A. Mnich in www.JavaExchange.com.
     * <br>With thanks.
     * @return String - never null;
     */
    public static final String createUUID() {
        return RandomUtil.createUUID( RandomUtil.getRandom() );
    }
    
    /**
     * Return a random universal identifier.  Fairly secure but slower.
     * Based on class RandomGUID by Marc A. Mnich in www.JavaExchange.com.
     * <br>With thanks.
     * @return String - never null;
     */
    public static final String createSecureUUID() {
        return RandomUtil.createUUID( RandomUtil.getSecureRandom() );
    }  
    
    /**
     * Generate a <code>String</code> of random characters of the
     * passed length;
     * @param length
     * @return
     */
    public static final String createString( final int length ) {
        if ( length < 1 ) {
			throw new IllegalArgumentException();
		}
        char[] array = new char[length];  
        Random random = RandomUtil.getRandom();
        for ( int i = 0; i < length; i ++ ) {            
            array[i] = RandomUtil.CHARACTERS[ random.nextInt( RandomUtil.CHARACTERS.length )];
        }
        return new String (array); 
    }
    
    /**
     * Generate a <code>String</code> of random characters of the
     * of between the passed miniumum and maximum lengths (inclusive).
     * If a zero length is permitted then this is probably for a reason 
     * so this is made moree likely (approx 1 in 3).
     * @param length
     * @return
     */
    public static final String createString( int minLength, final int maxLength ) {
        if ( minLength < 0 ) {
			throw new IllegalArgumentException();
		}
        if ( maxLength <= minLength ) {
			throw new IllegalArgumentException();
		}
        // 
        if ( minLength == 0 ) {
            if ( RandomUtil.getRandom().nextFloat() < 0.3f ) {
                return ""; //$NON-NLS-1$
            }
            minLength = 1; 
        }
        int length = minLength + RandomUtil.getRandom().nextInt( maxLength - minLength + 1);
        assert length >= minLength;
        assert length <= maxLength;
        String s = RandomUtil.createString( length );
        assert s.length() == length;
        return s;
    } 
    
    /**
     * Create a gibberish sentance with spaces and finishing in a full stop.
     * @return
     */
    public static String createRandomSentence() {
        int numWords = RandomUtil.createInt( 5, 15 );
        StringBuffer sb = new StringBuffer() ;
        for ( int i=0 ; i < numWords ; i ++ ) {
            sb.append( RandomUtil.createString( 1, 10 ) );
            sb.append( " " ); //$NON-NLS-1$
        }
        sb.append( "." ); //$NON-NLS-1$
        return sb.toString();
    }
    
    /**
     * Create mutlipe gibberish sentences sperated by line breaks.
     * @return
     */
    public static String createRandomSentences() {
        StringBuffer sb = new StringBuffer() ;
        int numLines = RandomUtil.createInt( 5, 25 );
        for ( int i=0 ; i < numLines ; i++ ) {
           sb.append( RandomUtil.createRandomSentence() );
           sb.append( System.getProperty( "line.separator" ) ) ; //$NON-NLS-1$
        }
        sb.append( RandomUtil.createRandomSentence() );
        return sb.toString();       
    }
    
    /**
     * Convenience method - max must be greater than min
     * @param min
     * @param max
     * @return
     */
    public static int createInt( final int min, final int max ) {
        if ( max < min ) {
			throw new IllegalArgumentException();
		}
        return ( min + RandomUtil.getRandom().nextInt( max - min ) );
    }
    
    /**
     * Returns a float based on the two int min and max values
     * All values must/will be positive
     * @param min
     * @param max
     * @return
     */
    public static float createFloat( final int min, final int max ) {
        if ( min < 0 ) {
			throw new IllegalArgumentException();
		}
        if ( max < 0 ) {
			throw new IllegalArgumentException();
		}
    	if ( max < min ) {
			throw new IllegalArgumentException();
		}
        float seed = RandomUtil.getRandom().nextFloat();
        return Math.abs( min + seed*(max-min) );
    }
    
    /**
     * Returns a BigDecimal based on the two int min and max 
     * values to the passed number of decimal points.
     * @param min
     * @param max
     * @param decimalpoints
     * @return
     */
    public static BigDecimal createBigDecimal( final int min, final int max, final int decimalpoints ) {
    	return new BigDecimal( RandomUtil.createDecimalPoints( min, max, decimalpoints ) );
    }
    
    /**
     * Returns a String representing a number based on the two int min and max 
     * values to the passed number of decimal points.
     * All values must/will be positive
     * <br>'orrible algorithm - would be nice to tidy
     * @param min
     * @param max
     * @return
     */
    public static String createDecimalPoints( final int min, final int max, final int decimalpoints ) {
        if ( min < 0 ) {
			throw new IllegalArgumentException();
		}
        if ( max < 0 ) {
			throw new IllegalArgumentException();
		}
        if ( max < min ) {
			throw new IllegalArgumentException();
		}
        if ( decimalpoints < 0 ) {
			throw new IllegalArgumentException();
		}
        long multiplier = (long)Math.pow( 10, decimalpoints );
        long lMin = multiplier * min;
        long lMax = multiplier * max;
        String s = String.valueOf( RandomUtil.createLong( lMin, lMax ) );
        int pointIndex = s.length() - decimalpoints;
        StringBuffer sb = new StringBuffer();
        if ( pointIndex < 0 ) {
        	for ( int i=0 ; i < Math.abs( pointIndex) ; i++ ) {
        		sb.append( "0" ); //$NON-NLS-1$
            }
            pointIndex = 0;
        }
        else if ( pointIndex == 0 ) {
            sb.append( "0" ); //$NON-NLS-1$
        }
        else {
        	sb.append( s.substring( 0, pointIndex ) );
        }
        sb.append ( "." ); //$NON-NLS-1$
        String end = s.substring( pointIndex );
        sb.append( end );
        int numTrailingZeroesRequired = decimalpoints - end.length();
        if (  numTrailingZeroesRequired > 0  ) {
        	for ( int i=0 ; i < numTrailingZeroesRequired; i++ ) {
                sb.append( "0" ); //$NON-NLS-1$
            }
        }
        return sb.toString(); 
    }
    
    
    /**
     * A method that really should exist on the <code>Random</code> class.
     * Returns a <code>long</code> between (inclusive) the two values passed.
     * Max must be bigger than min.
     * @param min
     * @param max
     * @return
     */
    public static long createLong( final long min, final long max ) {
        if ( max <= min ) {
			throw new IllegalArgumentException() ;
		}
        
        long between = 0;  // set it to something
        
        // optimising for speed throughout
        // this weird implementation avoid 'orrible problems (ask Mike)
        if ( Integer.MAX_VALUE > ( max -  min ) ) {
            between =  min + RandomUtil.getRandom().nextInt( (int)(max -min) ) ;
        }
        else { 
	        int min_highorder = (int)( min >>> 32);    
	        int max_highorder = (int)( max >>> 32);
	        if ( max_highorder == min_highorder ) {
	            between = min + RandomUtil.getRandom().nextInt( Integer.MAX_VALUE );
	        }
	        else {
	           int between_highorder = RandomUtil.createInt( min_highorder, max_highorder );
	           if ( between_highorder == min_highorder ) {
	               between = min + RandomUtil.getRandom().nextInt( Integer.MAX_VALUE );
	           }
	           else if ( between_highorder == max_highorder ) {
	               between = max - RandomUtil.getRandom().nextInt( Integer.MAX_VALUE );
	           }
	           else {
	               between = ((long)between_highorder<<32)
	                       + RandomUtil.getRandom().nextInt( Integer.MAX_VALUE );
	           }
	        }
        }     
        assert between >= min;
        assert between <= max;
        return between;
    }
    
    /**
     * Returns a random date between the two passed dates (inclusive).
     * @param earliest
     * @param latest
     * @return
     */
    public static final Date createDate( final Date earliest, final Date latest ) {
        if ( earliest == null ) {
			throw new IllegalArgumentException();
		}
        if ( latest == null ) {
			throw new IllegalArgumentException();
		}
        if ( latest.before( earliest ) ) {
			throw new IllegalArgumentException();
		}
        if ( RandomUtil.isSameDate( earliest, latest ) ) {
			throw new IllegalArgumentException();
		}
        
        long betweenTimeMillis = RandomUtil.createLong( earliest.getTime(), 
                						  	 latest.getTime() );
        Date between = new Date( betweenTimeMillis );
        
        assert earliest.before( between ) || earliest.equals( between ) ;
        assert latest.after( between ) || latest.equals( between ) ;
        return between;
    }
    
    /**
     * Convenience method - returns a date hinted at by the argument or
     * throws an <code>IllegalArgumentException</code> (type-safe enumeration
     * seemed overkill for a test class).
     * 
     * @param dateHint
     * @return
     */
    public static final Date createDate( final int dateHint ) {
        Calendar earliest = Calendar.getInstance();
        Calendar latest = Calendar.getInstance();
        switch( dateHint ) {
            case DATE_HINT_LAST_YEAR:
                earliest.add( Calendar.YEAR, -1 );
                break;
            case DATE_HINT_LAST_MONTH:
                earliest.add( Calendar.MONTH, -1 );
                break;   
            case DATE_HINT_LAST_WEEK:
                earliest.add( Calendar.DATE, -7 );
                break;  
            case DATE_HINT_NEXT_YEAR:
                latest.add( Calendar.YEAR, 1 );
                break;
            case DATE_HINT_NEXT_MONTH:
                latest.add( Calendar.MONTH, 1 );
                break;   
            case DATE_HINT_NEXT_WEEK:
                latest.add( Calendar.DATE, 7 );
                break;  
            case DATE_HINT_DOB:
                earliest.add( Calendar.YEAR, -65 );
                latest.add( Calendar.YEAR, -20 );
                earliest.get( Calendar.YEAR );
                break;                  
            default:
                throw new IllegalArgumentException();
        }
        Date earliestDate = earliest.getTime();
        Date latestDate = latest.getTime();
        assert !RandomUtil.isSameDate( earliestDate, latestDate );
        return RandomUtil.createDate( earliestDate, latestDate );
    }
    
    /**
     * Returns <code>true</code> three quarters (approx!) of the time.
     * @return
     */
    public static final boolean threeQuartersLikely() {
        return RandomUtil.getRandom().nextFloat() < 0.75f ;
    }
    
    /**
     * Returns <code>true</code> one quarter (approx!) of the time.
     * @return
     */
    public static final boolean oneQuarterLikely() {
        return RandomUtil.getRandom().nextFloat() < 0.25f ;
    }
    
    
    /**
     * Returns <code>true</code> two thirds (approx!) of the time.
     * @return
     */
    public static final boolean twoThirdsLikely() {
        return RandomUtil.getRandom().nextFloat() < 0.67f ;
    }
    
    /**
     * Returns <code>true</code> one half (approx!) of the time.
     * @return
     */
    public static final boolean oneHalfLikely() {
        return RandomUtil.getRandom().nextFloat() < 0.5f ;
    }
    
    /**
     * Returns <code>true</code> one third (approx!) of the time.
     * @return
     */
    public static final boolean oneThirdLikely() {
        return RandomUtil.getRandom().nextFloat() < 0.33f ;
    }
    
    /**
     * Return a random element form the array
     * @param array
     * @return
     */
    public static Object getRandomElement( final Object[] array ) {
    	if (array == null) {
			throw new IllegalArgumentException();
		}
        if (array.length < 1 ) {
			throw new IllegalArgumentException();
		}
        return array[ RandomUtil.createInt( 0, array.length ) ];
    }
    
    /**
     * Return a random element from a list
     * @param array
     * @return
     */
    public static Object getRandomElement( final List<?> list) {
        if ( list  == null) {
			throw new IllegalArgumentException();
		}
        int size = list.size();
        if ( size < 1 ) {
			throw new IllegalArgumentException();
		}
        return list.get( RandomUtil.createInt( 0, size ) );
    }

    /**
     * common implementation.
     * 
     * <p>
     * TODO: replace with JDK library function?
     * 
     * @param random
     * @return
     */
    private static final String createUUID(
    		final Random random ) {
        StringBuffer preMD5 = new StringBuffer();
        long time = System.currentTimeMillis();
        long rand = random.nextLong();

        // this StringBuffer can be a long as you need; the MD5
        // hash will always return 128 bits.
        preMD5.append( RandomUtil.getHostName() );
        preMD5.append( ":" ); //$NON-NLS-1$
        preMD5.append(Long.toString( time ) );
        preMD5.append( ":" ); //$NON-NLS-1$
        preMD5.append(Long.toString( rand ) );
        
        MessageDigest digest = RandomUtil.getMessageDigest();
        digest.update( preMD5.toString().getBytes());

        byte[] array = digest.digest();
        StringBuffer postMD5  = new StringBuffer();
        for (int j = 0; j < array.length; ++j) {
            int b = array[j] & 0xFF;
            if (b < 0x10) {
				postMD5.append('0');
			}
            postMD5.append(Integer.toHexString(b));
        }
        
        // convert to the standard format for GUID
        // e.g. C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
        StringBuffer formatted = new StringBuffer();
        formatted.append( postMD5.substring(0, 8).toUpperCase() );
        formatted.append( "-" ); //$NON-NLS-1$
        formatted.append( postMD5.substring(8, 12).toUpperCase() );
        formatted.append( "-" ); //$NON-NLS-1$
        formatted.append( postMD5.substring(12, 16).toUpperCase() );
        formatted.append( "-" ); //$NON-NLS-1$
        formatted.append( postMD5.substring(16, 20).toUpperCase() );
        formatted.append( "-" ); //$NON-NLS-1$
        formatted.append( postMD5.substring(20).toUpperCase() );       

        return formatted.toString();
    }
    
    // lazy instantiation
    private static final MessageDigest getMessageDigest() {
        if ( RandomUtil._digest == null ) {
            synchronized( RandomUtil.class) {
                // double-checked locking not totally safe but not really
                // an issue here
                if ( RandomUtil._digest == null ) {
		            try {
		                RandomUtil._digest = MessageDigest.getInstance( RandomUtil.PREFERRED_DIGEST_ALGORITHM );
		            }
		            catch ( NoSuchAlgorithmException nsae ) {
		                throw new AssertionError( nsae.toString() );
		            }
                }
            }
        }
        assert RandomUtil._digest != null;
        return RandomUtil._digest;
    }
    
    // lazy instantiation
    private static final Random getRandom() {
        if ( RandomUtil._random == null ) {
            synchronized( RandomUtil.class) {
                // double-checked locking not totally safe but not really
                // an issue here
                if ( RandomUtil._random == null ) {
                    RandomUtil._random = new Random();
                }
            }
        }
        assert RandomUtil._random != null;
        return RandomUtil._random;
    }
    
    // lazy instantiation
    private static final Random getSecureRandom() {
        if ( RandomUtil._secureRandom == null ) {
            synchronized( RandomUtil.class) {
                // double-checked locking not totally safe but not really
                // an issue here
                if ( RandomUtil._secureRandom == null ) {
		            // this will be slow
		            RandomUtil._secureRandom = new SecureRandom();
                }
            }
        }
        assert RandomUtil._secureRandom != null;
        return RandomUtil._secureRandom;
    }
    
    // lazy instantiation
    private static final String getHostName() {
        if ( RandomUtil._hostName == null ) {
            synchronized( RandomUtil.class) {
                // double-checked locking not totally safe but not really
                // an issue here
                if ( RandomUtil._hostName == null ) {
		            try {
		                RandomUtil._hostName = InetAddress.getLocalHost().getHostName();
		            }
		            catch( UnknownHostException uhe ) {   
		                RandomUtil._hostName = uhe.toString() +  uhe.hashCode() ;
		            }
                }
            }
        }
        assert RandomUtil._hostName != null;
        return RandomUtil._hostName;
    }
	
	// utility function
    private static boolean isSameDate( final Date date1, final Date date2 ) {
        if ( date1 == null ) {
			throw new IllegalArgumentException();
		}
        if ( date2 == null ) {
			throw new IllegalArgumentException();
		}
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime( date1 );
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime( date2 );       
        if ( cal1.get( Calendar.YEAR) != cal2.get( Calendar.YEAR ) ) {
			return false;
		}
        if ( cal1.get( Calendar.MONTH ) != cal2.get( Calendar.MONTH ) ) {
			return false;
		}
        return( cal1.get( Calendar.DATE ) == cal2.get( Calendar.DATE ) ); 
    }
    
    
    private RandomUtil() {
        // prevent instantiation
    }
    

}
