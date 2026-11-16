import 'package:flutter_test/flutter_test.dart';
import 'package:mtb_smart_classes/main.dart';

void main() {
  testWidgets(
    'MTB Smart Classes app smoke test',
    (WidgetTester tester) async {
      await tester.pumpWidget(
        const MTBSmartClassesApp(),
      );

      expect(
        find.text('MTB Smart Classes'),
        findsOneWidget,
      );

      expect(
        find.text('MTB SMART CLASSES'),
        findsOneWidget,
      );
    },
  );
}