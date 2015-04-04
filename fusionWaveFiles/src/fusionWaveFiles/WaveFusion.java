package fusionWaveFiles;

import java.io.ByteArrayInputStream;
import java.io.File;
//import java.util.logging.Level;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WaveFusion
{
	private final static int STANDARD_FRAME_RATE = 8000;
	private final static int STANDARD_BIT = 16;
	private WaveFile _waveFormatOne;
	private WaveFile _waveFormatTwo;
	private int whatIUsed = 1;

	/**
	 * Making the merger of the two identical files on the chunk
	 * 
	 * @param firsFile
	 * @param secondFile
	 * @param fullPathToNewFile
	 */
	public void makeFusion(String firsFile, String secondFile, String fullPathToNewFile) throws Exception
	{
		_waveFormatOne = new WaveFile(new File(firsFile));
		_waveFormatTwo = new WaveFile(new File(secondFile));

		whatIUsed = 1;
		if (!testNormalized(_waveFormatOne))
			throw new Exception("Unable to normalize file: " + _waveFormatOne.getFileName());
		whatIUsed = 2;
		if (!testNormalized(_waveFormatTwo))
			throw new Exception("Unable to normalize file: " + _waveFormatTwo.getFileName());

		AudioFormat afEtalon = _waveFormatOne.getAudioFormat();
		byte[] first = _waveFormatOne.getData();
		byte[] second = _waveFormatTwo.getData();
		byte[] samples = new byte[first.length + second.length];
		int i = 0;
		int j = 0;
		while (i < first.length)
		{
			samples[j] = first[i];
			i++;
			j++;
		}
		i = 0;
		while (i < second.length)
		{
			samples[j] = second[i];
			i++;
			j++;
		}
		AudioSystem.write(
				new AudioInputStream(new ByteArrayInputStream(samples), afEtalon, _waveFormatOne.getFramesCount() + _waveFormatTwo.getFramesCount()),
				AudioFileFormat.Type.WAVE, new File(fullPathToNewFile));
	}

	/**
	 * check whether it is necessary to normalize
	 * 
	 * @param audioFormat
	 * @return true if appropriate under the standard or normalized
	 * @return false not normalized
	 */
	private boolean testNormalized(WaveFile waveFile)
	{
		if ((waveFile.getAudioFormat().getFrameRate() == STANDARD_FRAME_RATE) && (waveFile.getAudioFormat().getSampleSizeInBits() == STANDARD_BIT))
		{
			return true;
		}
		else
		{
			return doNormalized(waveFile);
		}
	}

	private boolean doNormalized(WaveFile waveFile)
	{
		// AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian)
		AudioFormat _af = new AudioFormat((float) STANDARD_FRAME_RATE, (int) STANDARD_BIT, (int) 1, true, false);
		File _f = new File(System.getProperty("user.dir") + "\\tmpWaveFusion.wav");

		try
		{
			AudioInputStream lowResAIS;
			if (AudioSystem.isConversionSupported(_af, waveFile.getAudioFormat()))
			{
				lowResAIS = AudioSystem.getAudioInputStream(_af, AudioSystem.getAudioInputStream(new File(waveFile.getFileName())));

				AudioSystem.write(lowResAIS, AudioFileFormat.Type.WAVE, _f);
				if (whatIUsed == 1)
					_waveFormatOne = new WaveFile(_f);
				else
					_waveFormatTwo = new WaveFile(_f);
				return true;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
