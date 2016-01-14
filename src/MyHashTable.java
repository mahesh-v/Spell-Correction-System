
public class MyHashTable<AnyType> {
	private final int DEFAULT_SIZE = 13;
	private int count;
	private AnyType[] array;
	
	@SuppressWarnings("unchecked")
	public MyHashTable() {
		count = 0;
		array = (AnyType[]) new Object[DEFAULT_SIZE];
	}
	public boolean insert(AnyType x)
	{
		int hashIndex = hash(x);
		boolean insertSuccess = linearProbeAndInsert(x, hashIndex);
		if (insertSuccess) {
			count++;
			ensureCapacity();
			return true;
		}
		return false;
		
	}
	public boolean contains(AnyType x) {
		int indexOfX = hash(x);
		for (int i = indexOfX; i < array.length; i++) {
			if(x.equals(array[i]))
				return true;
			if(array[i] == null)
				return false;
		}
		for (int i = 0; i < indexOfX; i++) {
			if(x.equals(array[i]))
				return true;
			if(array[i] == null)
				return false;
		}
		return false;
	}
	
	private int hash(AnyType x) {
		int hc = x.hashCode();
		if(hc < 0)
			hc = hc * (-1);
		return hc%array.length;
	}
	private boolean linearProbeAndInsert(AnyType x, int hashIndex) {
		if(contains(x))
			return true;
		for (int i = hashIndex; i < array.length; i++) {
			if(array[i] == null)
			{
				array[i] = x;
				return true;
			}
		}
		for (int i = 0; i < hashIndex; i++) {
			if(array[i] == null)
			{
				array[i] = x;
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	private void ensureCapacity() {
		if(count < array.length/2)
			return;
		AnyType[] oldArray = array;
		int newArrayLength = nextPrime(2*oldArray.length);
		array = (AnyType[]) new Object[newArrayLength];
		for (int i = 0; i < oldArray.length; i++) {
			if(oldArray[i] != null)
			{
				linearProbeAndInsert(oldArray[i], hash(oldArray[i]));
			}
		}
	}
	
	private int nextPrime( int n )
    {
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }
	
	private boolean isPrime( int n )
    {
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }
}
