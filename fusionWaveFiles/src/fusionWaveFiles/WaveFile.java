package fusionWaveFiles;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Simple class to work with wave-files.
 */
public class WaveFile
{

	private int INT_SIZE = 4;
	public final int NOT_SPECIFIED = -1;
	private int sampleSize = NOT_SPECIFIED;
	private long framesCount = NOT_SPECIFIED;
	private byte[] data = null; // Array of bytes representing the audio data
	private AudioInputStream ais = null;
	private AudioFormat af = null;
	private String fullPath = null;

	/**
	 * Creates an object from the specified wave-file
	 *
	 * @param file - wave-file
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	WaveFile(File file) throws UnsupportedAudioFileException, IOException
	{
		if (!file.exists())
		{
			throw new FileNotFoundException(file.getAbsolutePath());
		}

		// call file
		fullPath = file.getPath();

		// receive audio data stream
		ais = AudioSystem.getAudioInputStream(file);

		// receive information on the format
		af = ais.getFormat();

		// number of frames in the file
		framesCount = ais.getFrameLength();

		// sample size in bytes
		sampleSize = af.getSampleSizeInBits() / 8;

		// data size in bytes
		long dataLength = framesCount * af.getSampleSizeInBits() * af.getChannels() / 8;

		// read in the memory of all the data from the file again
		data = new byte[(int) dataLength];
		ais.read(data);
	}

	/**
	 * Creates an object from an array of integers
	 *
	 * @param sampleSize - number of bytes occupied by the sampled
	 * @param sampleRate - frequency
	 * @param channels - channel number
	 * @param samples - array of values (data)
	 * @throws Exception if the sample size is smaller than necessary
	 * To store a variable of type int
	 */
	WaveFile(int sampleSize, float sampleRate, int channels, int[] samples) throws Exception
	{

		if (sampleSize < INT_SIZE)
		{
			throw new Exception("sample size < int size");
		}

		this.sampleSize = sampleSize;
		this.af = new AudioFormat(sampleRate, sampleSize * 8, channels, true, false);
		this.data = new byte[samples.length * sampleSize];

		// complete data
		for (int i = 0; i < samples.length; i++)
		{
			setSampleInt(i, samples[i]);
		}

		framesCount = data.length / (sampleSize * af.getChannels());
		ais = new AudioInputStream(new ByteArrayInputStream(data), af, framesCount);
	}

	public void setAIS(AudioInputStream _ais) throws IOException
	{
		ais = _ais;
		af = ais.getFormat();
	}

	public AudioInputStream getAIS()
	{
		return ais;
	}

	public String getFileName()
	{
		return fullPath;
	}

	public AudioFormat getAudioFormat()
	{
		return af;
	}

	public byte[] getData()
	{
		return Arrays.copyOf(data, data.length);
	}

	public int getSampleSize()
	{
		return sampleSize;
	}

	public double getDurationTime()
	{
		return getFramesCount() / getAudioFormat().getFrameRate();
	}

	public long getFramesCount()
	{
		return framesCount;
	}

	public void saveFile(File file) throws IOException
	{
		AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(data), af, framesCount), AudioFileFormat.Type.WAVE, file);
	}

	public String getFullWaveInformation()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Name of file: " + "\n");
		sb.append("Format of the audio data: " + getAudioFormat() + "\n");
		sb.append("Number of bytes occupied by one sample: " + getSampleSize() + "\n");
		sb.append("Signal duration in seconds: " + getDurationTime() + "\n");
		sb.append("Number of frames (frames) in the file: " + getFramesCount() + "\n");
		return sb.toString();
	}

	/**
	* Returns the value of the sample to the serial number. If data
	* Recorded in 2 channels, it is necessary to take into account that the samples of the left and
	* The right channel alternate. For example, the sample is at number one
	* The first sample of the left channel, sample number two is the first sample of the right
	* Channel sample number three is the second sample of the left channel, etc ..
	*
	* @param SampleNumber - sample number starting with 0
	* @return The value of the sample
	*/
	public int getSampleInt(int sampleNumber)
	{

		if (sampleNumber < 0 || sampleNumber >= data.length / sampleSize)
		{
			throw new IllegalArgumentException("sample number is can't be < 0 or >= data.length/" + sampleSize);
		}

		// Array of bytes to represent the sample
		// (In this case the whole number)
		byte[] sampleBytes = new byte[sampleSize];

		// Read from the data bytes that match
		// Specified sample number
		for (int i = 0; i < sampleSize; i++)
		{
			sampleBytes[i] = data[sampleNumber * sampleSize + i];
		}

		// Convert bytes into an integer
		int sample = ByteBuffer.wrap(sampleBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

		return sample;
	}

	/**
	 * Sets the value of the sample
	 *
	 * @param sampleNumber - sample number
	 * @param sampleValue - value of sample
	 */
	public void setSampleInt(int sampleNumber, int sampleValue)
	{

		// integer in the form of a byte array
		byte[] sampleBytes = ByteBuffer.allocate(sampleSize).order(ByteOrder.LITTLE_ENDIAN).putInt(sampleValue).array();

		// Write consistently received bytes
		// In place that corresponds to the specified
		// Sample number
		for (int i = 0; i < sampleSize; i++)
		{
			data[sampleNumber * sampleSize + i] = sampleBytes[i];
		}
	}
}