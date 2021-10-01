import 'package:flutter/material.dart';

// To use channels, must import both services and async
import 'package:flutter/services.dart';
import 'dart:async';

//import 'package:com.DubaiLizards.so_cyc';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Dashboard'),
          backgroundColor: Colors.green,
        ),
        body: const Center(
          child: TestWidget(), // Calls my new stateful widget
        ),
      ),
    );
  }
}

// Widgets for testing
class TestWidget extends StatefulWidget {
  const TestWidget({Key? key}) : super(key: key);

  @override
  _TestWidgetState createState() => _TestWidgetState();
}

// State of the testing widget
class _TestWidgetState extends State<TestWidget> {
  // We assign a method channel within the state of the widget
  static const platformMethodChannel = MethodChannel("socyc/test");
  final _suggestions = <String>[];
  final _biggerFont = const TextStyle(fontSize: 18);
  String _nativeMessage = '';

  Future<void> _getTestText() async {
    String _message = "";
    try{
      // Try to get result of the linked function
      final String result = await platformMethodChannel.invokeMethod("GetTestText");
      _message = result;
    } on PlatformException catch (e) {
      _message = "Error: ${e.message}.";
    }
    // Update the current state of the widget, tells the widget to update its look
    setState(() {
      _nativeMessage = _message; // Get the message from the linked function
    });
  }

  // Build this UI Element
  @override
  Widget build(BuildContext context) {
    _getTestText();
    return Scaffold (
      appBar: AppBar(
        //title: Text('Events'),
        title: Text(_nativeMessage), // Get the message from the java code
        backgroundColor: Colors.lightGreen,
      ),
      body: _buildList(), // Test code to generate a scrollable list
    );
  }

  // Build a single row of the list
  Widget _buildRow(String text) {
    return ListTile(
      title: Text(
        text,
        style: _biggerFont,
      ),
    );
  }

  // Build a scrollable list
  Widget _buildList(){
    // Flutter list object (ScrollRect)
    return ListView.builder(
        padding: const EdgeInsets.all(16),
        itemBuilder: (BuildContext _context, int i) {
          return _buildRow("Test " + i.toString());
          /*
          // Add a one-pixel-high divider widget before each row in the ListView.
          if (i.isOdd) {
            return Divider(
              color: Colors.grey,
            );
          }
          // Some hacky way of getting the legit rows
          // i -> The number of filled rows in this list
          // The syntax "i ~/ 2" divides i by 2 and returns an integer result.
          // For example: 1, 2, 3, 4, 5 becomes 0, 1, 1, 2, 2.
          // This calculates the actual number of occupied rows minus the divider widgets.
          final int index = i ~/ 2;
          if (index >= _suggestions.length) {
            _suggestions.add("Event " + index.toString());
          }
          return _buildRow(_suggestions[index]);
           */
        }
    );
  }
}