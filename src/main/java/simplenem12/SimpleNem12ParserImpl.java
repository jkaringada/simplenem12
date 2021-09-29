package simplenem12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.LinkedList;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {

	private static final String DELIMITER = ",";

	private static final String RECORD_TYPE100 = "100";
	private static final String RECORD_TYPE200 = "200";
	private static final String RECORD_TYPE300 = "300";
	private static final String RECORD_TYPE900 = "900";

	/**
	 * Parses Simple NEM12 file.
	 * 
	 * @param simpleNem12File file in Simple NEM12 format
	 * @return Collection of <code>MeterRead</code> that represents the data in the
	 *         given file.
	 */

	@Override
	public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) {

		boolean isHeader = false;
		boolean isTrailer = false;
		int rec200Count = 0;

		Collection<MeterRead> meterRead = new LinkedList<MeterRead>();
		try (BufferedReader br = new BufferedReader(new FileReader(simpleNem12File))) {
			String line;
			MeterRead mR = null;
			while ((line = br.readLine()) != null) {
				String recordType = line.split(DELIMITER)[0];

				if (isValidRecordType(recordType, RECORD_TYPE100)) {
					isHeader = true;
				} else if (isValidRecordType(recordType, RECORD_TYPE900)) {
					isTrailer = true;
					if (mR != null)
						meterRead.add(mR);
				} else if (isValidRecordType(recordType, RECORD_TYPE200)) {
					String[] rec200 = line.split(DELIMITER);
					rec200Count++;
					if (rec200Count == 2) {
						meterRead.add(mR);
						rec200Count--;
					}
					mR = new MeterRead(rec200[1], EnergyUnit.valueOf(rec200[2]));
				} else if (isValidRecordType(recordType, RECORD_TYPE300)) {
					process_type300_Records(mR, line);
				}

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (isHeader && isTrailer)
			return meterRead;
		else {
			return null;

		}
	}

	/**
	 * Extracts volume data from the record and stores in the MeterRead object
	 * 
	 * @param mR   an instance of a meter record is passed
	 * @param line is the record type 300 line consisting of data to store meter
	 *             volume data
	 * 
	 */

	private void process_type300_Records(MeterRead mR, final String line) {
		try {
			String[] rec300 = line.split(DELIMITER);
			final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
			final LocalDate dt = LocalDate.parse(rec300[1], dtf);
			MeterVolume mv = new MeterVolume(new BigDecimal(rec300[2]), Quality.valueOf(rec300[3]));
			mR.getVolumes().put(dt, mv);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param value      is the record type string read from the NEM12 file
	 * @param recordType the valid REcord type being compared against
	 * @return whether that record type is a valid record type
	 */
	private boolean isValidRecordType(String value, String recordType) {
		return value.equals(recordType);
	}

}
