from flask import Flask, jsonify, render_template, request, redirect, url_for  
from pymongo import MongoClient  
from datetime import datetime  

app = Flask(__name__)  

def get_db_connection():  
    client = MongoClient("mongodb://localhost:27017/")  
    db = client["medical_service"]  
    return db  

@app.route('/')  
def home():  
    return render_template('home.html')  

@app.route('/add_patient', methods=['GET', 'POST'])  
def add_patient():  
    if request.method == 'POST':  
        full_name = request.form['full_name']  
        date_of_birth = request.form['date_of_birth']  
        gender = request.form['gender']  
        address = request.form['address']  

        db = get_db_connection()  
        patient = {  
            "full_name": full_name,  
            "date_of_birth": date_of_birth,  
            "gender": gender,  
            "address": address  
        }  
        db.patients.insert_one(patient)  
        print(f"Added patient: {patient}")  # In thông tin bệnh nhân đã thêm
        return redirect(url_for('home'))  

    return render_template('add_patient.html')  

@app.route('/add_doctor', methods=['GET', 'POST'])  
def add_doctor():  
    if request.method == 'POST':  
        full_name = request.form['full_name']  
        specialization = request.form['specialization']  
        phone_number = request.form['phone_number']  
        email = request.form['email']  

        db = get_db_connection()  
        doctor = {  
            "full_name": full_name,  
            "specialization": specialization,  
            "phone_number": phone_number,  
            "email": email  
        }  
        db.doctors.insert_one(doctor)  
        print(f"Added doctor: {doctor}")  # In thông tin bác sĩ đã thêm
        return redirect(url_for('home'))  
        
    return render_template('add_doctor.html')  

@app.route('/add_appointment', methods=['GET', 'POST'])  
def add_appointment():  
    if request.method == 'POST':  
        patient_full_name = request.form['patient_full_name']  
        doctor_full_name = request.form['doctor_full_name']  
        appointment_date_str = request.form['appointment_date']  
        reason = request.form['reason']  
        status = request.form['status']  

        db = get_db_connection()  
        patient = db.patients.find_one({"full_name": patient_full_name})  
        doctor = db.doctors.find_one({"full_name": doctor_full_name})  

        if patient and doctor:  
            appointment_date = datetime.strptime(appointment_date_str, "%Y-%m-%d")  
            appointment = {  
                "patient_id": patient["_id"],  
                "doctor_id": doctor["_id"],  
                "appointment_date": appointment_date,  
                "reason": reason,  
                "status": status  
            }  
            db.appointments.insert_one(appointment)  
            print(f"Added appointment: {appointment}")  # In thông tin cuộc hẹn đã thêm
            return redirect(url_for('home'))  
        else:  
            return "Could not find patient or doctor."  

    return render_template('add_appointment.html')  

@app.route('/report')  
def report():  
    db = get_db_connection()  
    appointments = db.appointments.aggregate([  
        {  
            "$lookup": {  
                "from": "patients",  
                "localField": "patient_id",  
                "foreignField": "_id",  
                "as": "patient_info"  
            }  
        },  
        {  
            "$lookup": {  
                "from": "doctors",  
                "localField": "doctor_id",  
                "foreignField": "_id",  
                "as": "doctor_info"  
            }  
        }  
    ])  

    report_data = []  
    for appointment in appointments:  
        patient = appointment['patient_info'][0]  
        doctor = appointment['doctor_info'][0]  
        report_data.append({  
            "patient_name": patient['full_name'],  
            "date_of_birth": patient['date_of_birth'],  
            "gender": patient['gender'],  
            "address": patient['address'],  
            "doctor_name": doctor['full_name'],  
            "specialization": doctor['specialization'],  
            "reason": appointment['reason'],  
            "date": appointment['appointment_date']  
        })  
    print(f"Generated report data: {report_data}")  # In dữ liệu báo cáo đã tạo

    return render_template('report.html', report_data=report_data)  

if __name__ == '__main__':  
    app.run(debug=True)
